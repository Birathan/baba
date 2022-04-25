package Commands;

import Database.DBUtil;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TodoCommand extends Command {
   HashMap<Integer,Integer> idMapping = new HashMap<>();
   final String todoTable;
   public TodoCommand(){
      super("TODO COMMAND",
              "> **!todo list ** - view task list (taskID, task, completion status)\n" +
                      "> **!todo add [string] ** - add task to list \n" +
                      "> **!todo complete [integer] ** - mark task as complete\n" +
                      "> **!todo remove [integer] ** - remove task from list");
      Dotenv dotenv = Dotenv.load();
      this.todoTable = dotenv.get("TODO_TABLE");
      this.updateMapping();
   }

   public void updateMapping(){
      idMapping = new HashMap<>();
      int id = 1;
      Connection conn = DBUtil.getConnection();
      try {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("select * from "+this.todoTable +" order by id");
         while(rs.next()){
            idMapping.put(id, rs.getInt(1));
            id += 1;
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }


   @Override
   public void execute(MessageChannel channel, String[] args) {
      if (args.length == 0){
         channel.sendMessage(this.getErrorMessage()).queue();
         return;
      }
      String commandType = args[0];


      Connection conn = DBUtil.getConnection();
      try {
         Statement stmt = conn.createStatement();
         PreparedStatement st;
         String output = "";
         ResultSet rs;
         EmbedBuilder eb;
         switch(commandType){
            case "list":
               this.updateMapping();
               rs = stmt.executeQuery("SELECT COUNT(*) AS taskCount FROM " + this.todoTable);
               rs.next();

               int taskCount = rs.getInt("taskCount");
               eb = new EmbedBuilder();
               eb.setTitle("Task List :ledger: :");
               eb.setColor(new Color(0x10B981));


               if (taskCount == 0){
                  System.out.println("no tasks");
                  eb.setDescription("\nYou currently have no tasks, you can add tasks using **!todo add [string]**");
                  channel.sendMessageEmbeds(eb.build()).queue();
                  break;
               }

               rs = stmt.executeQuery("SELECT * FROM "+this.todoTable +" ORDER BY id");

               int task_num = 0;
               int finished = 0;
               String[] tasks = new String[idMapping.size()];
               int id = 1;
               ArrayList<String> col1 = new ArrayList<>();
               ArrayList<String> col2 = new ArrayList<>();
               ArrayList<String> col3 = new ArrayList<>();

               while(rs.next()){
                  boolean isComplete = rs.getBoolean(3);
                  col1.add(String.valueOf(id));
                  col2.add(isComplete? ":white_check_mark:":":x:");
                  col3.add( rs.getString(2));
                  tasks[id-1] ="  "+(isComplete? ":white_check_mark:":":x:") +"   - " +id +" "+ rs.getString(2);
                  finished += 1;
                  task_num += isComplete ? 1: 0;
                  id += 1;
               }
               eb.setDescription("Here is a list of all your tasks:");

               eb.addField("ID",String.join("\n", col1), true);
               eb.addField("Task",String.join("\n", col3), true);
               eb.addField("Done",String.join("\n", col2), true);
               channel.sendMessageEmbeds(eb.build()).queue();

               break;
            case "add":
               String task = String.join(" ", Arrays.copyOfRange(args,1,args.length));
               st = conn.prepareStatement("INSERT INTO "+this.todoTable +"(task, completed) VALUES(?, FALSE)");
               st.setString(1, task);
               st.executeUpdate();
               channel.sendMessage("**Task added successfully!**").queue();
               break;
            case "complete":
               st = conn.prepareStatement("UPDATE "+this.todoTable +" SET completed=TRUE WHERE id=?");
               st.setInt(1,this.idMapping.get(Integer.parseInt(args[1])));
               st.executeUpdate();
               output += "**Task "+args[1]+" successfully marked as complete!**";
               channel.sendMessage(output).queue();

               break;
            case "remove":
               this.updateMapping();
               st = conn.prepareStatement("delete from "+this.todoTable +" WHERE id=?");
               st.setInt(1, this.idMapping.get(Integer.parseInt(args[1])));
               st.execute();
               output = "**Task removed successfully!**";
               channel.sendMessage(output).queue();
               break;
            default:
               channel.sendMessage(this.getErrorMessage()).queue();
               break;
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

      DBUtil.closeConnection(conn);


   }
}

