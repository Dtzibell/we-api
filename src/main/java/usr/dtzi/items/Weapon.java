package usr.dtzi.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public record Weapon(
    @JsonProperty("money") double price,
    String itemCode, 
    @JsonProperty("item") ItemDetails details) {
}
