import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
   private String commandName;
   private String documentation;

   public Command(String commandName,String documentation){
      this.documentation = documentation;
      this.commandName = commandName;
   }

   public String getDocumentation(){
      return this.documentation;
   }

   public String getCommandName(){
      return this.commandName;
   }

   public abstract void execute(MessageChannel channel, String[] args);
}
