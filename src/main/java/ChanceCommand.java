import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Arrays;
import java.util.Random;

public class ChanceCommand extends Command {

   public ChanceCommand(){
      super("chance roll for coin toss , dice roll and choice of a list of options");
   }

   @Override
   public void execute(MessageChannel channel, String[] args) {
      if (args.length == 0){
         channel.sendMessage(this.getDocumentation()).queue();
         return;
      }
      String chanceType = args[0];
      Random rand = new Random();
      String response;
      switch (chanceType) {
         case "dice":
            response = "I rolled a ... ";
            channel.sendMessage(response + (rand.nextInt(6) + 1)).queue();
            break;
         case "coin":
            response = "Looks like it came up ... ";
            channel.sendMessage(response + ((rand.nextInt(2) == 1) ? "tails" : "heads")).queue();
            break;
         case "choose":
            String[] choices = Arrays.copyOfRange(args, 1, args.length);
            response = "I spun a wheel and got: ";
            channel.sendMessage(response + choices[rand.nextInt(choices.length)]).queue();
            break;
         default:
            channel.sendMessage(this.getDocumentation()).queue();
            break;
      }
   }
}
