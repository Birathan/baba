package Commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class GoogleCommand extends Command {
   final String googleApiKey;
   final String googleCX;

   public GoogleCommand(){
      super("GOOGLE SEARCH","> **!google [string]** - get top 5 google search results for given query");
      Dotenv dotenv = Dotenv.load();
      this.googleApiKey = dotenv.get("GOOGLE_API_KEY");
      this.googleCX = dotenv.get("GOOGLE_CX");
   }

   @Override
   public void execute(MessageChannel channel, String[] args) {
      if (args.length == 0){
         channel.sendMessage(this.getErrorMessage()).queue();
         return;
      }
      try {
         String query = URLEncoder.encode(String.join(" ",args), "UTF-8");
         URL url = new URL("https://www.googleapis.com/customsearch/v1?" +
                 "key=" + this.googleApiKey +
                 "&cx=" + this.googleCX + "&q=" + query +
                 "&alt=json"+"&start="+"0"+"&num="+"5");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Accept", "application/json");
         BufferedReader br = new BufferedReader(new InputStreamReader(
                 (conn.getInputStream())));

         String res = br.lines().collect(Collectors.joining());
         JSONObject object = new JSONObject(res);
         JSONArray searchResults = object.getJSONArray("items");

         for (int i=0; i < searchResults.length();i++) {
            channel.sendMessage(searchResults.getJSONObject(i).getString("link")).queue();
         }

      } catch (IOException e) {
         e.printStackTrace();
      }

   }
}
