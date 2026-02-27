package usr.dtzi;

import usr.dtzi.items.Equipment;
import usr.dtzi.items.filter.ItemFilter;
import usr.dtzi.json.JSONParser;
import usr.dtzi.api.APIPresets;
import usr.dtzi.items.filter.ItemBounds;

import java.io.File;
import java.io.IOException;
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
      new File("fullJSONs").mkdir();
      new File("filteredJSONs").mkdir();

      for (String itemCode : items) {
        IO.println("Processing: " + itemCode);
        var amount = 1000;
        APIPresets.getLatestTransactions(amount, itemCode);

        IO.println("Finished processing: " + itemCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
