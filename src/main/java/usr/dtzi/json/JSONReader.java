package usr.dtzi.json;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;


import java.time.format.DateTimeFormatterBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
/**
 * A JSON processor class.
 */
public class JSONReader {

  private String contents;
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Class to parse cursors to dates.
   *
   */
  public class CursorParser {

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

  private CursorParser parser;

  public JSONReader(JsonNode n) {
    this.contents = n.toString();
  }

  // s has to be strictly formatted as a JSON
  public JSONReader(String s) {
    this.contents = s;
  }

  public String readFile(File f) throws IOException {
    var freader = new FileReader(f);
    List<String> lines = freader.readAllLines();
    freader.close();
    return String.join("\n", lines);
  }

  public JSONReader(File f) {
    try {
      this.contents = readFile(f);
    } catch (IOException e) {
      System.out.printf("""
          File %s does not exist""", f.getAbsolutePath());
    }
  }

  public void setContents(File f) {
    try {
      this.contents = readFile(f);
    } catch (IOException e) {
      System.out.printf("""
          File %s does not exist""", f.getAbsolutePath());
    }
  }

  public LocalDateTime parseDate() {
    var cursor = mapper.readTree(this.contents).findPath("nextCursor");
    var date = parser.parse(cursor.asString());
    return date;
  }
  
  public void setContents(String s) {
    this.contents = s;
  }
   
  public String getContents() {
    return this.contents;
  }

  /** 
   * @param _class the class of the object that is serialized in {@link JSONReader#contents}.
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

  public Optional<LocalDateTime> getFileCreationDate() throws IOException {
    try (var reader = new BufferedReader(new FileReader(this.contents))) {
      return Optional.of(this.mapper.
        readValue(reader.readLine(), ZonedDateTime.class)
        .toLocalDateTime());
    } catch (FileNotFoundException e) {
      return Optional.empty();
    }
  }

}
