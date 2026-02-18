package usr.dtzi;

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

  public URIBuilder addParam(String key, Object value) {
    this.uri = this.uri.concat(formattedKeyValuePair(key, value));
    return this;
  }

  private String formattedKeyValuePair(String key, Object value) {
    if (!(value instanceof Integer)) {
      value = "\"" + value + "\"";
    }
    key = "\"" + key + "\"";
    return URLEncoder.encode(key + ":" + value, charset);
  }

  public URI build() throws URISyntaxException {
    this.uri = this.uri + rightBracket;
    IO.println("Final URI: " + this.uri);
    return new URI(this.uri);
  }
}
