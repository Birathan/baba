package Database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
   public static Connection getConnection(){
      Dotenv dotenv = Dotenv.load();
      String connection_url = dotenv.get("PG_CONNECTION_URL");
      String user = dotenv.get("PG_USERNAME");
      String pass = dotenv.get("PG_PASSWORD");
      Connection con = null;
      try {
         Class.forName("org.postgresql.Driver");
         con = DriverManager.getConnection(connection_url, user, pass);
      } catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
      }
      return con;
   }

   public static void closeConnection(Connection con){
      if(con != null){
         try {
            con.close();
         } catch(SQLException e){
            e.printStackTrace();
         }
      }
   }
}
