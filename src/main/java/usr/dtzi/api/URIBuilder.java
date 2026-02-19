package usr.dtzi.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Used to add parameters to an API URL.
 */
public class URIBuilder {

  private String uri;
  private HashMap<String, Object> params = new HashMap<>();

  private Charset charset = Charset.defaultCharset();
  private String leftBracket = URLEncoder.encode("{", charset);
  private String rightBracket = URLEncoder.encode("}", charset);

  public URIBuilder(String URI) {
    this.uri = URI + "?input=" + leftBracket;
  }

  /** 
   * @param   key   the key of an argument.
   * @param   value the value of an argument.
   * @return  a URIBuilder with the added parameter.
   */
  // TODO: What if params were being added to a hashmap and then arsed at build()?
  public URIBuilder addParam(String key, Object value) {
    //this.params.put(key, value);
    this.uri = this.uri.concat(formattedKeyValuePair(key, value));
    return this;
  }

  /** 
   * @param   k
   * @param   v
   * @return  a formatted key-value pair as a string.
   *
   * <p> return value is encoded and returned in the format {@code <key>:<value>,}
   * (notice the comma at the end)
   */
  private String formattedKeyValuePair(String k, Object v) {
    if (!(v instanceof Integer)) {
      v = "\"" + v + "\"";
    }
    k = "\"" + k + "\"";
    return URLEncoder.encode(
        k + ":" + v
        , charset)
      +",";
  }

  /** 
   * @return a URI that contains the string of the URIBuilder.
   *
   * finishes URI formatting and converts to a URI.
   */
  public URI build() throws URISyntaxException {
    this.params.forEach((k, v) -> this.uri.concat(formattedKeyValuePair(k, v)));
    this.uri = 
      this.uri.substring(0, this.uri.length()-1) // removes the last comma;
      + rightBracket; 
    IO.println("Final URI: " + this.uri);
    return new URI(this.uri);
  }
}
