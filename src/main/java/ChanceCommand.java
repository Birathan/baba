import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChanceCommand extends Command {
   private final List<String> eightball_responses = Arrays.asList("It is certain", "Without a doubt", "You may rely on it", "Yes definitely",
           "It is decidedly so", "As I see it, yes", "Most likely", "Yes", "Outlook good", "Signs point to yes",
           "Reply hazy try again", "Better not tell you now", "Ask again later", "Cannot predict now",
           "Concentrate and ask again", "Don’t count on it", "Outlook not so good", "My sources say no",
           "Very doubtful", "My reply is no");
   public ChanceCommand(){
      super("CHANCE COMMAND",
              "> **!chance coin**  - simulate coin toss\n" +
              "> **!chance dice**  - simulate dice roll\n" +
             "> **!chance 8ball [string]** - simulate magic 8ball\n" +
              "> **!chance choose [choice1, choice2, etc...]** - choose randomly between list of choices");
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
            response = "I rolled a: ";
            channel.sendMessage(response + "**"+(rand.nextInt(6) + 1) + "**").queue();
            break;
         case "coin":
            response = "Looks like it came up: ";
            channel.sendMessage(response + "**"+((rand.nextInt(2) == 1) ? "tails" : "heads")+"**").queue();
            break;
         case "choose":
            String[] choices = Arrays.copyOfRange(args, 1, args.length);
            response = "I spun a wheel and got: ";
            channel.sendMessage(response + "**"+choices[rand.nextInt(choices.length)] +"**").queue();
            break;
         case "8ball":
            Collections.shuffle(this.eightball_responses);
            channel.sendMessage("**"+this.eightball_responses.get(rand.nextInt(this.eightball_responses.size()))+"**").queue();
            break;
         default:
            channel.sendMessage(this.getDocumentation()).queue();
            break;
      }
   }
}
