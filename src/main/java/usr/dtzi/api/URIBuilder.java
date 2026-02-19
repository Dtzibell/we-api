package usr.dtzi.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class URIBuilder {

  String uri;

  Charset charset = Charset.defaultCharset();
  String leftBracket = URLEncoder.encode("{", charset);
  String rightBracket = URLEncoder.encode("}", charset);

  public URIBuilder(String URI) {
    this.uri = URI + "?input=" + leftBracket;
  }

  /* @param key the key of an argument
   * @param value the value of an argument
   * @return a URIBuilder with the added parameter
   */
  public URIBuilder addParam(String key, Object value) {
    this.uri = this.uri.concat(formattedKeyValuePair(key, value));
    return this;
  }

  /** @param key
   * @param value
   * @return a formatted key-value pair as a string.
   *
   * return value is encoded and returned in the format "<key>:<value>,"
   * (notice the comma at the end)
   */
  private String formattedKeyValuePair(String key, Object value) {
    if (!(value instanceof Integer)) {
      value = "\"" + value + "\"";
    }
    key = "\"" + key + "\"";
    return URLEncoder.encode(
        key + ":" + value
        , charset)
      +",";
  }

  /** @return a URI that contains the string of the URIBuilder.
   *
   * finishes URI formatting and converts to a URI.
   */
  public URI build() throws URISyntaxException {
    this.uri = this.uri.substring(0, this.uri.length()-1) + rightBracket;
    IO.println("Final URI: " + this.uri);
    return new URI(this.uri);
  }
}
