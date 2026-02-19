package usr.dtzi.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;

public class ResponseManager {

  String response;
  PrintWriter writer;

  public ResponseManager(HttpResponse<String> s) {
    this.response = s.body();
  }

  public ResponseManager(String s) {
    this.response = s;
  }

  public ResponseManager() {
    this.response = "";
  }

  public void writeResponse(File f) throws FileNotFoundException, IOException {
    var writer = new PrintWriter(f);
    writer.write(this.response);
    writer.close();
  }

  public void setResponse(String s) {
    this.response = s;
  }

  public String getResponse() {
    return this.response;
  }

  /**
   * @param s
   *
   * <p> appends the body of {@code s} to {@link ResponseManager#response}.
   * Ends with a newline.
   *
   */
  public void appendResponse(HttpResponse<String> s) {
    this.response = this.response.concat(s.body() + "\n");
  }

  public void appendResponse(String s) {
    this.response = this.response.concat(s);
  }
}
