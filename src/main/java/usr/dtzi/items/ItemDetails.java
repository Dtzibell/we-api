package usr.dtzi.items;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public record ItemDetails(Skills skills) implements Serializable {
}
