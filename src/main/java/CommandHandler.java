import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import io.github.cdimascio.dotenv.Dotenv;
import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import Commands.*;

public class CommandHandler extends ListenerAdapter {
   public static Map<String, Command> commandMapping = new HashMap<>();
   public static void main(String[] args) throws LoginException {
      System.out.println("discord bot v13");
      Dotenv dotenv = Dotenv.load();
      String token = dotenv.get("DISCORD_BOT_TOKEN");
      JDA jda = JDABuilder.createDefault(token).build();
      jda.addEventListener(new CommandHandler());
      commandMapping.put("!ping", new PingCommand());
      commandMapping.put("!chance", new ChanceCommand());
      commandMapping.put("!quote", new QuoteCommand());
      commandMapping.put("!todo", new TodoCommand());
      commandMapping.put("!reminder", new ReminderCommand());
      commandMapping.put("!reddit", new RedditSearchCommand());
      commandMapping.put("!google", new GoogleCommand());
   }

   @Override
   public void onMessageReceived(MessageReceivedEvent event)
   {
      Message msg = event.getMessage();
      MessageChannel channel = msg.getChannel();
      String query = msg.getContentRaw().trim();

      if (query.startsWith("b!")) {
         String[] args = query.split("\\s+");
         String commandName = args[0];
         if (commandName.equals("!help")){
            StringBuilder doc = new StringBuilder();
            for (Map.Entry<String, Command> entry: commandMapping.entrySet()){
               Command c = entry.getValue();
               doc.append("**").append(c.getCommandName()).append("**\n").append(c.getDocumentation()).append("\n\n");
            }
            channel.sendMessage(doc.toString()).queue();
         } else{
            args = Arrays.copyOfRange(args,1,args.length);
            Command cmd = commandMapping.get(commandName);

            if (cmd != null){
               cmd.execute(channel, args);
            } else {
               channel.sendMessage("Command **"+commandName+"** not found, type **!help** to get a list of available commands").queue();
            }
         }

      }

   }

}
