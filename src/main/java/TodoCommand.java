import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.*;
import java.util.Arrays;

public class TodoCommand extends Command{

   public TodoCommand(){
      super("Help maintain a daily todo list");
   }

   public int getNextId(Statement st, String table){
      int id = 0;
      try {
         ResultSet rs = st.executeQuery("select * from "+table);
         while(rs.next()){
            id += 1;
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return id;
   }


   @Override
   public void execute(MessageChannel channel, String[] args) {
      Dotenv dotenv = Dotenv.load();
      String connection_url = dotenv.get("PG_CONNECTION_URL");
      String user = dotenv.get("PG_USERNAME");
      String pass = dotenv.get("PG_PASSWORD");
      String todoTable = dotenv.get("TODO_TABLE");
      String commandType = args[0];
      try {
//         Class.forName("com.mysql.jdbc.Driver");
         Connection conn = DriverManager.getConnection(
                 connection_url, user, pass);

         Statement stmt = conn.createStatement();
         PreparedStatement st;
         String output = "";
         ResultSet rs;
         switch(commandType){
            case "list":
               rs = stmt.executeQuery("SELECT * FROM "+todoTable+" ORDER BY id");
               //
               // System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

               int task_num = 0;
               int finished = 0;
               while(rs.next()){
                  boolean isComplete = rs.getBoolean(3);
                  output += " ["+(isComplete? "✓":"-") +"]   - " +rs.getInt(1) +" "+ rs.getString(2)+"\n";
                  finished += 1;
                  task_num += isComplete ? 1: 0;
               }
               output ="Task List: `["+task_num+"/" +finished+ "] Completed ` \n" + output;
               break;
            case "add":
               int id = getNextId(stmt, todoTable);
               st = conn.prepareStatement("INSERT INTO "+todoTable+" VALUES (?,?, FALSE)");
               st.setInt(1,id);
               st.setString(2, String.join(" ", Arrays.copyOfRange(args,1,args.length)));
               st.executeUpdate();
               output += "Task added successfully";
               break;
            case "complete":
               st = conn.prepareStatement("UPDATE "+todoTable+" SET completed=TRUE WHERE id=?");
               st.setInt(1,Integer.parseInt(args[1]));
               st.executeUpdate();
               output += "Task "+args[1]+" successfully marked as complete";
               break;
            default:
               output = getDocumentation();
         }
         conn.close();
         channel.sendMessage(output).queue();

      } catch (SQLException e) {
         System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}

