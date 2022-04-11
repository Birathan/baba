import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
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
               rs = stmt.executeQuery("SELECT COUNT(*) AS reminderCount FROM " + this.reminderTable);
               rs.next();
               int reminderCount = rs.getInt("reminderCount");

               EmbedBuilder eb = new EmbedBuilder();
               eb.setTitle("Reminder List :timer: :");
               eb.setColor(new Color(0x10B981));
               if (reminderCount == 0){
                  eb.setDescription("\nYou currently have no reminders, you can add tasks using **!reminder add [string]**");
                  channel.sendMessageEmbeds(eb.build()).queue();
                  break;
               }
               rs = stmt.executeQuery("SELECT * FROM " + this.reminderTable + " ORDER BY reminder_time");

               ArrayList<String> col1 = new ArrayList<>();
               ArrayList<String> col2 = new ArrayList<>();
               LocalTime rTime;
               LocalTime n = LocalTime.now();
               long remainingTime;
               while(rs.next()){
                  rTime = rs.getTime(2).toLocalTime();
                  remainingTime = SECONDS.between(n, rTime);
                  String time = remainingTime > (long)59*60+59 ? String.valueOf(rTime) : (remainingTime <= 0)? "passed ":remainingTime/60 + "m " +remainingTime%60 +"s";
                  col1.add("*"+time+"*");
                  col2.add(rs.getString(1));
                  output.append( "**").append(time).append("** ").append(rs.getString(1)).append("\n");
               }
               eb.addField("Time",String.join("\n",col1),true);
               eb.addField("Reminder",String.join("\n",col2),true);
//               eb.setDescription(output);
               channel.sendMessageEmbeds(eb.build()).queue();

               break;
            case "add":
               String reminderMsg = String.join(" ", Arrays.copyOfRange(args,2,args.length));
               DateTimeFormatter formatterTime1 = new DateTimeFormatterBuilder()
                       .parseCaseInsensitive().appendPattern("hh:mma").toFormatter(Locale.US);
               LocalTime reminderTime = LocalTime.parse(args[1], formatterTime1);
               LocalTime now = LocalTime.now();
               System.out.println("now "+ now + ": remindertime "+reminderTime);
               st = conn.prepareStatement("INSERT INTO "+this.reminderTable+" VALUES (?,?)");
               st.setString(1, reminderMsg);
               st.setTime(2, Time.valueOf(reminderTime));
               st.executeUpdate();
               output.append("Set reminder successfully.");
               channel.sendMessage("Reminder : " + reminderMsg).queueAfter(SECONDS.between(now, reminderTime), TimeUnit.SECONDS);
               channel.sendMessage(output.toString()).queue();
               break;

            default:
               output = new StringBuilder(getDocumentation());
               channel.sendMessage(output.toString()).queue();
         }
         DBUtil.closeConnection(conn);


      } catch (SQLException e) {
         System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
