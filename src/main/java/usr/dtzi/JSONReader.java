package usr.dtzi;

import java.io.File;
import tools.jackson.databind.ObjectMapper;
import usr.dtzi.items.Weapon;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {

  File f;
  ObjectMapper mapper = new ObjectMapper();

  public JSONReader(File f) {
    this.f = f;
  }

  public <T> List<List<T>> nodesToArray(Class<T> _class) {
    var root = this.mapper.readValue(this.f, new TypeReference<List<List<T>>>() {});
    System.out.println(root);
    return root;
    // System.out.println(root.size());
    // var objs = new ArrayList<T>();

    // for (JsonNode node : root) {

    //   var items = node.findPath("items");
    //   IO.println(items.size());

    //   for (int i = 0; i < items.size(); i++) {
    //     System.out.println(items.get(i));
    //     var obj = this.mapper.treeToValue(items.get(i), _class);
    //     objs.add(obj);
    //     IO.println(obj.toString());
    //   }

    // }
    // return objs;
  }
}
