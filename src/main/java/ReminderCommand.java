import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.*;

public class ReminderCommand extends Command{
   final String reminderTable;
   public ReminderCommand(){
      super("REMINDER COMMAND",
              "> **!reminder add [string]** - set a timed reminder\n" +
                      "> **!reminder list** - view all reminders");
      Dotenv dotenv = Dotenv.load();
      this.reminderTable = dotenv.get("REMINDER_TABLE");
   }

   @Override
   public void execute(MessageChannel channel, String[] args) {

      String commandType = args[0];
//      System.out.format("connection url %s ,username %s, pass %s", connection_url, user, pass);
      Connection conn = DBUtil.getConnection();

      try {
         Statement stmt = conn.createStatement();
         PreparedStatement st;
         StringBuilder output = new StringBuilder();
         ResultSet rs;
         switch(commandType){
            case "list":
               output.append("All Reminders: \n");
               rs = stmt.executeQuery("SELECT * FROM " + this.reminderTable + " ORDER BY reminder_time");

               while(rs.next()){
                  output.append(rs.getTime(2)).append(" ").append(rs.getString(1)).append("\n");
               }
               break;
            case "add":
               String reminderMsg = String.join(" ", Arrays.copyOfRange(args,2,args.length));
               DateTimeFormatter formatterTime1 = new DateTimeFormatterBuilder()
                       .parseCaseInsensitive().appendPattern("hh:mma").toFormatter(Locale.US);
               LocalTime reminderTime = LocalTime.parse(args[1], formatterTime1);
               LocalTime now = LocalTime.now();
               st = conn.prepareStatement("INSERT INTO "+this.reminderTable+" VALUES (?,?)");
               st.setString(1, reminderMsg);
               st.setTime(2, Time.valueOf(reminderTime));
               st.executeUpdate();
               output.append("Set reminder successfully.");
               channel.sendMessage("Reminder : " + reminderMsg).queueAfter(SECONDS.between(now, reminderTime), TimeUnit.SECONDS);
               break;

            default:
               output = new StringBuilder(getDocumentation());
         }
         DBUtil.closeConnection(conn);
         channel.sendMessage(output.toString()).queue();

      } catch (SQLException e) {
         System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
