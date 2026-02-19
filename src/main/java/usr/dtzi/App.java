package usr.dtzi;

import usr.dtzi.items.Equipment;
import usr.dtzi.items.filter.ItemFilter;
import usr.dtzi.json.JSONReader;
import usr.dtzi.api.APIPresets;
import usr.dtzi.items.filter.ItemBounds;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class App {
  public static void main(String[] args) {
    try {
      // App.connect();
      JSONReader reader = new JSONReader(new File("write.json"));
      List<Equipment> eq = reader.nodesToList(Equipment.class);
      var filter = new ItemFilter(eq);
      var filteredEq = filter.filter(ItemBounds::isWithin);
      IO.println(filteredEq.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void connect() {
    try {
      var writer = new PrintWriter(new File("write.json"));
      var response = APIPresets.getLatestTransactions(1000, "knife");
      writer.write(response);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
  
}
