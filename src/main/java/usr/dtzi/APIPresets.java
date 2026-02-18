package usr.dtzi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import tools.jackson.databind.ObjectMapper;

public class APIPresets {

  static HttpClient client = HttpClient.newHttpClient();
  static String BASE_URI = "https://api2.warera.io/trpc/";
  static ObjectMapper mapper = new ObjectMapper();
  static String API_KEY = System.getenv().get("WARERA_API_KEY");

  public static String getLatestTransactions(int amount, 
      String itemCode) throws 
    URISyntaxException, IOException, InterruptedException {

    String finalJson = "[";

    do {
      var URI = new URIBuilder(BASE_URI.concat(
            "transaction.getPaginatedTransactions"
            ))
        .addParam("limit", 100)
        .addParam("itemCode", itemCode)
        .addParam("cursor", "")
        .build();
      var req = HttpRequest
        .newBuilder(URI)
        .GET()
        .header("X-API-Key", API_KEY)
        .build();
      HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
      String cursor = mapper.readTree(response.body()).findPath("nextCursor").asString();
      IO.println(cursor);
      finalJson = finalJson.concat(response.body());
      amount -= 100;
    } while (amount > 0);

    return finalJson + "]";
  }

  public static void getLargestOrder(String itemCode) {
  }
}
