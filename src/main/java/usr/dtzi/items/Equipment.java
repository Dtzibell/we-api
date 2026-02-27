package usr.dtzi.items;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown=true)
public record Equipment(

    @JsonProperty("money") 
    double price,
    String itemCode, 
    @JsonProperty("item") 
    ItemDetails details,
    ZonedDateTime createdAt) 

    implements Serializable {

    public Integer getSkill1() {
      return this.details().skills().skill1();
    }
    public Integer getSkill2() {
      return this.details().skills().skill2();
    }
}
