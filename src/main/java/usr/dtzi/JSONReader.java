package usr.dtzi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import java.util.List;
import java.util.ArrayList;

public class JSONReader {

  File f;
  ObjectMapper mapper = new ObjectMapper();

  public JSONReader(File f) {
    this.f = f;
  }

  public <T> List<T> nodesToArray(Class<T> _class) throws FileNotFoundException, IOException {

    FileReader reader = new FileReader(f);
    List<String> lines = reader.readAllLines();
    var objs = new ArrayList<T>();
    for (String line : lines) {
      JsonNode root = this.mapper.readTree(line);
      var items = root.findPath("items");
      for (int i = 0; i < items.size(); i++) {
        var obj = this.mapper.treeToValue(items.get(i), _class);
        objs.add(obj);
      }
    }
    IO.println("Processed file: " + f.getName() + ", total objects: " + objs.size());
    reader.close();
    return objs;

    // for (JsonNode node : root) {

    //   var items = node.findPath("items");
    //   IO.println(items.size());


    // }
    // return objs;
  }
}
