package Commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.*;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dv8tion.jda.api.entities.MessageChannel;


public class RedditSearchCommand extends Command {
   public RedditSearchCommand(){
      super("REDDIT SEARCH","> **!reddit [string]** - get top 5 posts in the past week from given subreddit");
   }


   @Override
   public void execute(MessageChannel channel, String[] args) {
      if (args.length == 0){
         channel.sendMessage(this.getErrorMessage()).queue();
         return;
      }
      Dotenv dotenv = Dotenv.load();
      String username = dotenv.get("REDDIT_USER");
      String pass = dotenv.get("REDDIT_PW");
      String clientID = dotenv.get("REDDIT_CLIENTID");
      String secret = dotenv.get("REDDIT_SECRET");
      // Create our credentials
      if (username!=null && pass!=null && clientID!=null && secret!=null){
         //https://mattbdean.gitbooks.io/jraw/content/pagination.html
         Credentials credentials = Credentials.script(username, pass,
                 clientID, secret);
         UserAgent userAgent = new UserAgent("bot", "baba", "v0.1", username);
         // This is what really sends HTTP requests
         NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

         // Authenticate and get a RedditClient instance
         RedditClient reddit = OAuthHelper.automatic(adapter, credentials);
         DefaultPaginator<Submission> paginator = reddit.subreddit(args[0]).posts()
                 .limit(5) // 5 posts per page
                 .timePeriod(TimePeriod.WEEK) // of all time
                 .sorting(SubredditSort.TOP) // top posts
                 .build();
         Listing<Submission> submissions = paginator.next();
         for (Submission s: submissions.getChildren()){
            channel.sendMessage("https://www.reddit.com"+s.getPermalink()).queue();
         }
      } else {
         channel.sendMessage("Missing reddit credentials").queue();
      }


   }
}
