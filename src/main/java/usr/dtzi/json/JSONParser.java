package usr.dtzi.json;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.ArrayList;
import java.time.format.DateTimeFormatterBuilder;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;

/**
 * A JSON processor class.
 */
public class JSONParser {

  private String contents;
  private ObjectMapper mapper = new ObjectMapper();
  private CursorParser parser = new CursorParser();

  protected class CursorParser {

    /**
     * @param  cursor
     * @return a date representing the cursor
     *
     */
    public LocalDateTime parse(String cursor) {
      LocalDateTime ldt;

      var cursorSplit = Arrays.asList(cursor.split(" "));
      // should be getting data from day-of-week to time-zone
      cursor = String.join(" ", cursorSplit.subList(0, 6));
      var formatter = new DateTimeFormatterBuilder()
        .appendPattern("EEE MMM dd yyyy HH:mm:ss ")
        .appendLiteral("GMT")
        .appendOffset("+HHMM", "+0000")
        .toFormatter();
      ldt = ZonedDateTime.parse(cursor, formatter).toLocalDateTime();

      return ldt;

    }
  }

  public JSONParser(JsonNode n) {
    this.contents = n.toString();
  }

  /**
   * @param s contents that are strictly formatted as a json
   */
  public JSONParser(String s) {
    this.contents = s;
  }

  private String readFile(File f) throws IOException {
    var freader = new FileReader(f);
    List<String> lines = freader.readAllLines();
    freader.close();
    return String.join("\n", lines);
  }

  public JSONParser(File f) {
    try {
      this.contents = readFile(f);
    } catch (IOException e) {
      System.out.printf("""
          File %s does not exist""", f.getAbsolutePath());
    }
  }

  public JSONParser() {
  }

  public LocalDateTime parseDate() {
    var cursor = mapper.readTree(this.contents).findPath("nextCursor");
    var date = parser.parse(cursor.asString());
    return date;
  }

  /** 
   * @param _class the class of the object that is serialized in {@link JSONParser#contents}.
   * @return a List containing the objects
   */
  public <T> List<T> nodesToList(Class<T> _class) {

    List<String> lines = Arrays.asList(this.contents.split("\n"));
    var objs = new ArrayList<T>();
    for (String line : lines) {
      JsonNode root = this.mapper.readTree(line);
      var items = root.findPath("items");
      for (int i = 0; i < items.size(); i++) {
        var obj = this.mapper.treeToValue(items.get(i), _class);
        objs.add(obj);
      }
    }
    return objs;
  }

  public Optional<LocalDateTime> getDataCreationDate() throws IOException {
    try {
      var reader = new BufferedReader(Reader.of(this.contents));
      return Optional.of(this.mapper.
        readValue(reader.readLine(), ZonedDateTime.class)
        .toLocalDateTime());
    } catch (FileNotFoundException e) {
      // e.printStackTrace();
      return Optional.empty();
    } catch (Exception e) {
      IO.println("File not found.");
      return Optional.empty();
    }
  }

  public void setContents(String s) {
    this.contents = s;
  }
   
  private String getContents() {
    return this.contents;
  }

  public void setContents(File f) {
    try {
      this.contents = readFile(f);
    } catch (IOException e) {
      System.out.printf("""
          File %s does not exist""", f.getAbsolutePath());
    }
  }

}
