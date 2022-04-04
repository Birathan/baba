import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends Command {

   public PingCommand() {
      super("PING COMMAND","> **!ping** - check if the bot is up and running");
   }

   @Override
   public void execute(MessageChannel channel, String[] args) {
      channel.sendMessage("Pong!").queue();
   }
}
