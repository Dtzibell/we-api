package usr.dtzi.api;

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

  /** 
   * @param amount amount of transactions to get
   * @param itemCode the name of the item
   * @return A string containing the transactions in JSON. 
   *         It has a new line character every 100 items.
   *
   */
  public static String getLatestTransactions(int amount, 
      String itemCode) throws 
    URISyntaxException, IOException, InterruptedException {

    String finalJson = "";
    String cursor = "";

    do {

      var URI = new URIBuilder(BASE_URI.concat(
            "transaction.getPaginatedTransactions"
            ))
        .addParam("limit", 100)
        .addParam("itemCode", itemCode)
        .addParam("cursor", cursor)
        .build();

      var req = HttpRequest
        .newBuilder(URI)
        .GET()
        .header("X-API-Key", API_KEY)
        .build();

      HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
      cursor = mapper.readTree(response.body()).findPath("nextCursor").asString();
      IO.println(cursor);
      finalJson = finalJson.concat(response.body() + "\n");
      amount -= 100;

    } while (amount > 0);

    return finalJson;
  }

  public static void getLargestOrder(String itemCode) {
    // get the largest buy order of an item.
  }
}
