import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.*;

public class ReminderCommand extends Command{
   public ReminderCommand(){
      super("set a timed reminder");
   }

   @Override
   public void execute(MessageChannel channel, String[] args) {
      Dotenv dotenv = Dotenv.load();
      String connection_url = dotenv.get("PG_CONNECTION_URL");
      String user = dotenv.get("PG_USERNAME");
      String pass = dotenv.get("PG_PASSWORD");
      String reminderTable = dotenv.get("REMINDER_TABLE");
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
               output += "All Reminders: \n";
               rs = stmt.executeQuery("SELECT * FROM "+reminderTable+" ORDER BY reminder_time");

               while(rs.next()){
                  output += rs.getTime(2) + " " + rs.getString(1) + "\n";
               }
               break;
            case "add":
               String reminderMsg = String.join(" ", Arrays.copyOfRange(args,2,args.length));
               DateTimeFormatter formatterTime1 = new DateTimeFormatterBuilder()
                       .parseCaseInsensitive().appendPattern("hh:mma").toFormatter(Locale.US);
               LocalTime reminderTime = LocalTime.parse(args[1], formatterTime1);
               LocalTime now = LocalTime.now();
               st = conn.prepareStatement("INSERT INTO "+reminderTable+" VALUES (?,?)");
               st.setString(1, reminderMsg);
               st.setTime(2, Time.valueOf(reminderTime));
               st.executeUpdate();
               output+="Set reminder successfully.";
               channel.sendMessage("Reminder : " + reminderMsg).queueAfter(SECONDS.between(now, reminderTime), TimeUnit.SECONDS);
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
      return;
   }
}
