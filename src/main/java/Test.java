import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import io.github.cdimascio.dotenv.Dotenv;
import javax.security.auth.login.LoginException;

public class Test extends ListenerAdapter {
   public static void main(String[] args) throws LoginException {
      Dotenv dotenv = Dotenv.load();
      String token = dotenv.get("DISCORD_BOT_TOKEN");
      JDA jda = JDABuilder.createDefault(token).build();
      jda.addEventListener(new Test());
   }

   @Override
   public void onMessageReceived(MessageReceivedEvent event)
   {
      Message msg = event.getMessage();
      System.out.println("received Message");
      if (msg.getContentRaw().equals("!ping"))
      {
         MessageChannel channel = event.getChannel();
         long time = System.currentTimeMillis();
         channel.sendMessage("Pong!") /* => RestAction<Message> */
                 .queue(response /* => Message */ -> {
                    response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                 });
      }
   }

}
