package usr.dtzi.api;

import java.util.ArrayList;
import java.util.List;

import tools.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

public class ItemAccumulator<T> {

  List<T> items = new ArrayList<>();
  PrintWriter writer;
  ObjectMapper mapper = new ObjectMapper();
  Class<T> _class;

  private ItemAccumulator(Class<T> _class) {
    this._class = _class;
  }

  public static <T> ItemAccumulator<T> of(Class<T> _class) {
    return new ItemAccumulator<>(_class);
  }

  public void pushItems(List<T> items) {
    this.items.addAll(items);
  }

  public void pushItem(T item) {
    this.items.add(item);
  }

  public void writeItems(File f) {
    try (var writer = new BufferedWriter(new PrintWriter(f))) {
      writer.append(mapper.writeValueAsString(ZonedDateTime.now()));
      writer.newLine();
      for (T item : items) {
        writer.append(mapper.writeValueAsString(item)); 
        writer.newLine();
      }
    } catch (IOException e) {
      writer.flush();
      e.printStackTrace();
    }
  }
}
