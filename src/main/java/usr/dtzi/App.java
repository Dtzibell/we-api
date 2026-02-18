package usr.dtzi;

import java.io.File;
import java.io.PrintWriter;
import usr.dtzi.items.Weapon;

public class App {
  public static void main(String[] args) {
    JSONReader reader = new JSONReader(new File("write.json"));
    reader.nodesToArray(Weapon.class);
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
