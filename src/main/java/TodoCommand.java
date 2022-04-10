import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

public class TodoCommand extends Command{
   HashMap<Integer,Integer> idMapping = new HashMap<>();
   final String todoTable;
   public TodoCommand(){
      super("TODO COMMAND",
              "> **!todo list ** - view all tasks\n" +
                      "> **!todo add [string] ** - add task\n" +
                      "> **!todo complete [integer] ** - mark task as complete\n" +
                      "> **!todo remove [integer] ** - remove task from list\n");
      Dotenv dotenv = Dotenv.load();
      this.todoTable = dotenv.get("TODO_TABLE");
      this.updateMapping();
   }

   public void updateMapping(){
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

      String commandType = args[0];


//         Class.forName("com.mysql.jdbc.Driver");
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
               rs = stmt.executeQuery("SELECT * FROM "+this.todoTable +" ORDER BY id");
               //
               // System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

               int task_num = 0;
               int finished = 0;
               String[] tasks = new String[idMapping.size()];
               int id = 1;
               while(rs.next()){
                  boolean isComplete = rs.getBoolean(3);
                  tasks[id-1] ="  "+(isComplete? ":white_check_mark:":":x:") +"   - " +id +" "+ rs.getString(2);
                  finished += 1;
                  task_num += isComplete ? 1: 0;
                  id += 1;
               }
//               output ="Task List: `["+task_num+"/" +finished+ "] Completed ` \n" + output;
               eb = new EmbedBuilder();
               eb.setTitle("Task List :ledger: :");
               eb.setColor(new Color(0x10B981));
               eb.setDescription(String.join("\n", tasks));
               eb.addField("Completed","["+task_num+"/" +finished+"]", true);
               channel.sendMessageEmbeds(eb.build()).queue();

               break;
            case "add":
               String task = String.join(" ", Arrays.copyOfRange(args,1,args.length));
               st = conn.prepareStatement("INSERT INTO "+this.todoTable +"(task, completed) VALUES(?, FALSE)");
               st.setString(1, task);
               st.executeUpdate();
               eb = new EmbedBuilder();
               eb.setTitle("Task");
               eb.setColor(new Color(0x10B981));
               eb.setDescription("Task added successfully");
               eb.addField("Task",task, true);
               eb.addField("Completed","False", true);
               eb.addBlankField(false);
               channel.sendMessageEmbeds(eb.build()).queue();
               break;
            case "complete":
               st = conn.prepareStatement("UPDATE "+this.todoTable +" SET completed=TRUE WHERE id=?");
               st.setInt(1,this.idMapping.get(Integer.parseInt(args[1])));
               st.executeUpdate();
               output += "Task "+args[1]+" successfully marked as complete";
               channel.sendMessage(output).queue();

               break;
            case "remove":
               this.updateMapping();
               st = conn.prepareStatement("delete from "+this.todoTable +" WHERE id=?");
               st.setInt(1, this.idMapping.get(Integer.parseInt(args[1])));
               st.execute();
               output = "Task removed successfully";
               channel.sendMessage(output).queue();
               break;
            default:
               output = getDocumentation();
               channel.sendMessage(output).queue();

         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

      DBUtil.closeConnection(conn);


   }
}

