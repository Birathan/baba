import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
   private String commandName;

   public Command(String name){
      this.commandName = name;
   }

   public abstract String execute(MessageReceivedEvent event);
}
