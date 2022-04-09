import net.dv8tion.jda.api.entities.MessageChannel;

public abstract class Command {
   private final String commandName;
   private final String documentation;

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
