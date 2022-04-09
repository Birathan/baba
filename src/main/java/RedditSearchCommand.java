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

//import static sun.net.www.protocol.http.HttpURLConnection.userAgent;
//import org.apache.http.impl.client.HttpClientBuilder;

public class RedditSearchCommand extends Command {
   public RedditSearchCommand(){
      super("REDDIT SEARCH","> **!reddit [string]** - Search top 5 posts from subreddit in the past week");
   }


   @Override
   public void execute(MessageChannel channel, String[] args) {
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
