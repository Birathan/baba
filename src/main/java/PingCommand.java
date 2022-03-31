import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends Command {
   public PingCommand(String name) {
      super(name);
   }

   @Override
   public String execute(MessageReceivedEvent event) {
      return null;
   }
}
