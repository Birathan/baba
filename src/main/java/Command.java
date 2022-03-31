import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
   private String commandName;
   private String documentation;

   public Command(String documentation){
      this.documentation = documentation;
   }

   public String getDocumentation(){
      return this.documentation;
   }

   public abstract void execute(MessageChannel channel, String[] args);
}
