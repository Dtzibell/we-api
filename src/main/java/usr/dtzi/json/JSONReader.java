package usr.dtzi.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import java.util.List;
import java.util.ArrayList;
/**
 * A JSON processor class.
 */
public class JSONReader {

  /**
   * The {@link File} that is to be processed.
   */
  private File file;
  private ObjectMapper mapper = new ObjectMapper();

  public JSONReader(File f) {
    this.file = f;
  }

  public void setFile(File f) {
    this.file = f;
  }
   
  public File getFile(File f) {
    return this.file;
  }

  /** 
   * @param _class the class of the object that is serialized in {@link JSONReader#file}.
   * @return a List containing the objects
   */
  public <T> List<T> nodesToList(Class<T> _class) throws FileNotFoundException, IOException {

    FileReader reader = new FileReader(file);
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
    IO.println("Processed file: " + file.getName() + ", total objects: " + objs.size());
    reader.close();
    return objs;
  }
}
