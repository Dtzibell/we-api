package usr.dtzi.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public record Equipment(
    @JsonProperty("money") double price,
    String itemCode, 
    @JsonProperty("item") ItemDetails details) {

    public Integer getSkill1() {
      return this.details().skills().skill1();
    }
    public Integer getSkill2() {
      return this.details().skills().skill2();
    }
}
