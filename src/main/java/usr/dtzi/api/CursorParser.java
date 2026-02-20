package usr.dtzi.api;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.time.format.DateTimeFormatterBuilder;

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
  public ZonedDateTime parse(String cursor) {
    ZonedDateTime zdt;

    var cursorSplit = Arrays.asList(cursor.split(" "));
    // should be getting data from day-of-week to time-zone
    cursor = String.join(" ", cursorSplit.subList(0, 6));
    var formatter = new DateTimeFormatterBuilder()
      .appendPattern("EEE MMM dd yyyy HH:mm:ss ")
      .appendLiteral("GMT")
      .appendOffset("+HHMM", "+0000")
      .toFormatter();
    zdt = ZonedDateTime.parse(cursor, formatter);

    return zdt;

  }
}
