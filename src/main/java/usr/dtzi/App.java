package usr.dtzi;

import usr.dtzi.items.Equipment;
import usr.dtzi.items.filter.ItemFilter;
import usr.dtzi.json.JSONParser;
import usr.dtzi.api.APIPresets;
import usr.dtzi.items.filter.ItemBounds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileWriter;


import tools.jackson.databind.ObjectMapper;

public class App {
  public static void main(String[] args) {
    var items = Arrays.asList(
    "knife",
    "gun",
    "rifle",
    "sniper",
    "tank",
    "jet",
    "helmet1",
    "helmet2",
    "helmet3",
    "helmet4",
    "helmet5",
    "helmet6",
    "chest1",
    "chest2",
    "chest3",
    "chest4",
    "chest5",
    "chest6",
    "pants1",
    "pants2",
    "pants3",
    "pants4",
    "pants5",
    "pants6",
    "boots1",
    "boots2",
    "boots3",
    "boots4",
    "boots5",
    "boots6",
    "gloves1",
    "gloves2",
    "gloves3",
    "gloves4",
    "gloves5",
    "gloves6"
    );
    try {
      var fullJSONs = new File("fullJSONs");
      var filteredJSONs = new File("filteredJSONs");
      fullJSONs.mkdir(); filteredJSONs.mkdir();
      
      for (String itemCode : items) {
        IO.println("Processing: " + itemCode);
        var amount = 100;
        APIPresets.getLatestTransactions(amount, itemCode);
        IO.println("Finished processing: " + itemCode);
      }
      for (File f : fullJSONs.listFiles()) {
        var file = new File("filteredJSONs/" + f.getName());
        var writer = new BufferedWriter(new FileWriter(file));
        var fEq = App.filterEquipment(f, 0.7);
        var mapper = new ObjectMapper();
        fEq.forEach((eq) -> {
          try {
            writer.append(mapper.writeValueAsString(eq));
            writer.newLine();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
        writer.flush();
        writer.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<Equipment> filterEquipment(File f, double rollEff) throws FileNotFoundException, IOException {
    var mapper = new ObjectMapper();
    var reader = new BufferedReader(new FileReader(f));
    var file = reader.readAllLines();
    var fileContents = file.subList(1, file.size()-1);
    var unfEq = new ArrayList<Equipment>();
    fileContents.forEach((String item) -> 
        unfEq.add(mapper.readValue(item, Equipment.class)));
    var filter = new ItemFilter(unfEq);
    var fEq = filter.filter(rollEff);
    reader.close();
    return fEq;

  }
}
