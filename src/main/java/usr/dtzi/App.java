package usr.dtzi;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import tools.jackson.core.JsonPointer;

public class App {
  public static void main(String[] args) {
    try {
      var client = HttpClient.newHttpClient();
      var uri = new URIBuilder(
          "https://api2.warera.io/trpc/transaction.getPaginatedTransactions"
          )
        .addParam("limit", 10)
        .build();
      var env = System.getenv();
      var req = HttpRequest
        .newBuilder(uri)
        .GET()
        .header("X-API-Key", env.get("WARERA_API_KEY"))
        .build();

      IO.println("Request: " + req.toString());
      IO.println("Headers: " + req.headers());
      var response = client.send(req, BodyHandlers.ofString());
      IO.println(response.body());
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

void addParam(String key, String value) {
}
}
