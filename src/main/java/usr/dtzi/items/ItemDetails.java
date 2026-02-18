package usr.dtzi.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public record ItemDetails(Skills skills) {
}
