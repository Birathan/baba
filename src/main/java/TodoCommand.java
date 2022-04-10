import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TodoCommand extends Command{
   HashMap<Integer,Integer> idMapping = new HashMap<Integer,Integer>();
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
      PreparedStatement st;
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
               ArrayList<String> col1 = new ArrayList<String>();
               ArrayList<String> col2 = new ArrayList<String>();
               String[] tasks = new String[idMapping.size()];
               int id = 1;
               while(rs.next()){
                  boolean isComplete = rs.getBoolean(3);
//                  col1.add(rs.getString(2));
//                  col2.add(isComplete ? "True":"False");
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
//               eb.setThumbnail(":ledger:");
//               eb.setImage("https://i.imgur.com/AfFp7pu.png");
//               eb.setAuthor("author","https://i.imgur.com/AfFp7pu.png", "https://i.imgur.com/AfFp7pu.png");

               eb.addField("Completed","["+task_num+"/" +finished+"]", true);

//               eb.addField("Completed",String.join("\n",col2), true);
//               eb.addBlankField(true);
//               eb.addField("","under", true);
//               eb.add
//               eb.addField("","under", true);
//               eb.addBlankField(true);

//               eb.addField("under",task_num+"/" +finished, true);

//               eb.addBlankField(false);
//               eb.setAuthor("baba");
//               eb.setFooter("footer");
               output = "";
               channel.sendMessageEmbeds(eb.build()).queue();
//               channel.sendMessage(output).queue();

               break;
            case "add":
               String task = String.join(" ", Arrays.copyOfRange(args,1,args.length));
               st = conn.prepareStatement("INSERT INTO "+this.todoTable +"(task, completed) VALUES(?, FALSE)");
               st.setString(1, task);
               System.out.println(st.toString());
               st.executeUpdate();
               eb = new EmbedBuilder();
               eb.setTitle("Task");
               eb.setColor(new Color(0x10B981));
               eb.setDescription("Task added successfully");
               eb.addField("Task",task, true);
               eb.addField("Completed","False", true);
               eb.addBlankField(false);
//               eb.setAuthor("baba");
//               eb.setFooter("footer");
               output = "";
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

