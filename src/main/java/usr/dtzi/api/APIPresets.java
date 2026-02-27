package usr.dtzi.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;
import usr.dtzi.items.Equipment;
import usr.dtzi.json.JSONParser;

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
      String itemCode) throws InterruptedException, IOException {

    var limit = 100;
    AtomicInteger remaining = new AtomicInteger(amount);
    AtomicReference<String> cursor = new AtomicReference<>("");
    var accumulator = ItemAccumulator.of(Equipment.class);
    var parser = new JSONParser();

    File maybeOldFile = new File("fullJSONs/" + itemCode + "_" + amount + ".json");
    parser.setContents(maybeOldFile);
    Optional<LocalDateTime> fileCreationDate = parser.getDataCreationDate();
    boolean fileExisted = fileCreationDate.isPresent();

    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1); 

    ses.scheduleAtFixedRate(() -> {
      if (remaining.get() <= 0) {
        try {
          accumulator.writeItems(maybeOldFile);
          ses.shutdown();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        try {

          var URI = APIPresets.buildURI(limit, itemCode, cursor);
          var req = APIPresets.fetchPage(URI);

          HttpResponse<String> response = client.send(req,
              BodyHandlers.ofString());
          var body = response.body();
          parser.setContents(body);
          var responseDate = parser.parseDate();
          var items = parser.nodesToList(Equipment.class);

          if (fileExisted && responseDate.isBefore(fileCreationDate.get())) {
            var fileDate = fileCreationDate.get();
            var breakEvenIndex = 0;
            for (Equipment item : items) {
              if (item.createdAt().toLocalDateTime().isBefore(fileDate)) {
                break;
              } else breakEvenIndex++;
            }
            accumulator.pushItems(items.subList(0, breakEvenIndex));
            var fReader = new BufferedReader(new FileReader(maybeOldFile));
            fReader.readLine();
            var rest = remaining.get();
            while (remaining.get() > 0) {
              var line = fReader.readLine();
              if (line != null) {
                var item = mapper.readValue(line, Equipment.class);
                accumulator.pushItem(item);
                remaining.addAndGet(-1);
              } else break;
            }
            fReader.close();
            System.out.printf("Had %d items cached", rest);
            ses.shutdown();
            accumulator.writeItems(maybeOldFile);
            return;
          }

          accumulator.pushItems(items);
          cursor.set(mapper.readTree(response.body()).
              findPath("nextCursor").asString());
          remaining.addAndGet(-limit);

          IO.println("Next cursor: " + cursor.get());
          IO.println("Items remaining: " + remaining);

        } catch (JsonNodeException e) {
          System.out.printf("""
              Stopping at missing node for item: %s
              Reason: missing node (typical for old data)
              Processed %d items
              """.stripIndent(), itemCode, amount - remaining.get());
          try {
            accumulator.writeItems(maybeOldFile);
          } catch (Exception exc) {
            exc.printStackTrace();
          }
          ses.shutdown();
        } catch (Exception e) {
          e.printStackTrace();
          ses.shutdown();
        }
      }
    }, 0, 600, TimeUnit.MILLISECONDS);
    ses.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
  }

  public static void getLargestOrder(String itemCode) {
    // get the largest buy order of an item.
  }
}
