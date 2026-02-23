package usr.dtzi.api;

import java.io.File;
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
import usr.dtzi.json.JSONReader;

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

    File maybeOldFile = new File("fullJSONs/" + itemCode + "_" + amount + ".json");
    Optional<LocalDateTime> fileCreationDate = new JSONReader(maybeOldFile).getFileCreationDate();
    var fileExisted = fileCreationDate.isPresent();

    ResponseManager responseAccumulator = new ResponseManager();
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1); 

    ses.scheduleAtFixedRate(() -> {
      if (remaining.get() <= 0) {
        try {
          responseAccumulator.writeResponse(maybeOldFile);
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
          // var reader = new JSONReader(body);
          // var date = reader.parseDate();
          // if (fileExisted && date.isBefore(fileCreationDate.get())) {
          //    List<LocalDateTime> dates = APIPresets.getListOfDates(body);
          //    try {
          //    int earlierDateIndex = APIPresets.
          //      findEarlierDateIndex(dates, fileCreationDate.get());
          //    responseAccumulator.appendResponse("{\"result\":{\"data\":{\"items\":[");
          //    var eqs = reader.nodesToList(Equipment.class);
          //    for (int i = 0; i + 1 > earlierDateIndex; i++) {
          //      responseAccumulator.appendResponse(eqs.get(i).toString());
          //    }
          //    responseAccumulator.appendResponse("]}}}");
          //    } catch (OutOfBoundsException e)  {
          //      IO.println("There was no date that was earlier than" + 
          //          "file creation date" + fileCreationDate.toString());
          //    }
          // };

          // gets transaction dates and converts to LocalDateTimes
          // if (fileExisted) {
          //   if (matchingDateIndex.isPresent()) {
          //   }
          // }

          responseAccumulator.appendResponse(body + "\n");
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
            responseAccumulator.writeResponse(maybeOldFile);
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

  private static List<LocalDateTime> getListOfDates (String response) {
    var items = APIPresets.mapper.readTree(response).findPath("items");
    var dates = items.findValues("createdAt")
      .stream().map((JsonNode::asString)).toList();
    List<LocalDateTime> ldts = dates.stream().map((date) -> {
      return ZonedDateTime.parse(date).toLocalDateTime();
    }
      ).toList();
    return ldts;
  }

  /**
   * Thrown when array unexpectedly reached the end
   */
  private static class OutOfBoundsException extends Exception {
    public OutOfBoundsException() {
      super.getMessage();
    }
  }
  private static int findEarlierDateIndex (List<LocalDateTime> dates, LocalDateTime fileDate) throws OutOfBoundsException{
    for (int i = 0; i < dates.size(); i++) {
      if (dates.get(i).isBefore(fileDate)) {
        var matchingDateIndex = i;
        return matchingDateIndex;
      }
    }
    throw new APIPresets.OutOfBoundsException();
  }
}
