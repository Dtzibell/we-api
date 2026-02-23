package usr.dtzi.api;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;

public class APIPresets {

  static HttpClient client = HttpClient.newHttpClient();
  static String BASE_URI = "https://api2.warera.io/trpc/";
  static ObjectMapper mapper = new ObjectMapper();
  static String API_KEY = System.getenv().get("WARERA_API_KEY");

  static ScheduledExecutorService ses;

  private static URI buildURI(int limit, String itemCode, 
      AtomicReference<String> cursor) throws URISyntaxException {
    var uri = new URIBuilder(BASE_URI.concat(
                    "transaction.getPaginatedTransactions"
                    ))
                .addParam("limit", limit)
                .addParam("itemCode", itemCode)
                .addParam("cursor", cursor.get())
                .build();
    return uri;
  }
  
  private static HttpRequest fetchPage(URI uri) {
    var req = HttpRequest
      .newBuilder(uri)
      .GET()
      .header("X-API-Key", API_KEY)
      .build();
    return req;
  }

  /** 
   * @param amount amount of transactions to get
   * @param itemCode the name of the item
   *
   * <p> the API calls run every 600ms.
   */
  public static void getLatestTransactions(int amount, 
      String itemCode) throws InterruptedException {

    var limit = 100;
    AtomicInteger remaining = new AtomicInteger(amount);
    AtomicReference<String> cursor = new AtomicReference<>("");
    ResponseManager rmgr = new ResponseManager();
    File cache = new File("fullJSONs/" + itemCode + "_" + amount + ".json");
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1); 

    ses.scheduleAtFixedRate(() -> {
      if (remaining.get() <= 0) {
        try {
          rmgr.writeResponse(cache);
          ses.shutdown();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        try {
          var URI = APIPresets.buildURI(limit, itemCode, cursor);
          var req = APIPresets.fetchPage(URI);

          HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
          rmgr.appendResponse(response);
          cursor.set(mapper.readTree(response.body()).findPath("nextCursor").asString());
          IO.println("Next cursor: " + cursor);
          remaining.addAndGet(-100);
          IO.println("Items remaining: " + remaining);
        } catch (JsonNodeException e) {
          System.out.printf("""
              Stopping at missing node for item: %s
              Reason: missing node (typical for old data)
              Processed %d items
              """.stripIndent(), itemCode, remaining.get());
          try {
            rmgr.writeResponse(cache);
          } catch (Exception exc) {
            exc.printStackTrace();
          }
          ses.shutdown();
        } catch (Exception e) {
          e.printStackTrace();
          ses.shutdown();
        }
      }
    }, 0, 750, TimeUnit.MILLISECONDS);
    ses.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
  }

  public static void getLargestOrder(String itemCode) {
    // get the largest buy order of an item.
  }
}
