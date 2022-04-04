import net.dv8tion.jda.api.entities.MessageChannel;
//import org.apache.http.impl.client.HttpClientBuilder;

public class RedditSearchCommand extends Command {
   public RedditSearchCommand(){
      super("Search top 10 posts from reddit");
   }


   @Override
   public void execute(MessageChannel channel, String[] args) {
//      String a ="mJH4Ama1ZAXmombGZSQYZw";
//      // Information about the app
//      String userAgent = "jReddit: Reddit API Wrapper for Java";
//      String clientID = "mJH4Ama1ZAXmombGZSQYZw";
//      String redirectURI = "https://www.example.com/auth";
//// Assuming we have a 'script' reddit app
//      Credentials oauthCreds = Credentials.script(username, password, clientId, clientSecret);
//
//// Create a unique User-Agent for our bot
//      UserAgent userAgent = new UserAgent("bot", "my.cool.bot", "1.0.0", "myRedditUsername");
//
//// Authenticate our client
//      RedditClient reddit = OAuthHelper.automatic(new OkHttpNetworkAdapter(userAgent), oauthCreds);
//
//// Get info about the user
//      Account me = reddit.me().about();
   }
}
