package usr.dtzi.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

@JsonIgnoreProperties(ignoreUnknown=true)
public record Skills(
    @JsonProperty("attack")
    @JsonAlias({"dodge","armor","criticalChance", "precision"})
    int attack,
    int criticalChance) {
}
