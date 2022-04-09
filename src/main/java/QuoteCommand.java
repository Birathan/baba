import io.github.cdimascio.dotenv.Dotenv;
import net.dean.jraw.models.LiveUpdate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class QuoteCommand extends Command{

   public QuoteCommand(){
      super("QUOTE COMMAND","> **!quote** - this will generate a random quote");
   }
   @Override
   public void execute(MessageChannel channel, String[] args) {
      OkHttpClient client = new OkHttpClient();
      Dotenv dotenv = Dotenv.load();
      String KEY = dotenv.get("QUOTE_KEY");
      Request request = new Request.Builder()
              .url("https://quotes15.p.rapidapi.com/quotes/random/")
              .get()
              .addHeader("X-RapidAPI-Host", "quotes15.p.rapidapi.com")
              .addHeader("X-RapidAPI-Key", KEY)
              .build();

      try {
         Response response = client.newCall(request).execute();
         String jsonData = response.body().string();
         JSONObject obj = new JSONObject(jsonData);
         EmbedBuilder eb = new EmbedBuilder();
         eb.setDescription("*\""+obj.get("content")+"\"*");
         eb.setAuthor((String)(obj.getJSONObject("originator")).get("name"));
         eb.setColor(new Color(0x10B981));
         channel.sendMessageEmbeds(eb.build()).queue();

//         channel.sendMessage("*\""+obj.get("content")+"\"*"+" - "+obj.getJSONObject("originator").get("name")).queue();
      }  catch (IOException e) {
         e.printStackTrace();
      }
   }
}
