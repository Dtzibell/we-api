package usr.dtzi.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;

@JsonIgnoreProperties(ignoreUnknown=true)
public record Skills(
    @JsonProperty("attack")
    @JsonAlias({"dodge","armor","criticalDamages", "precision"})
    int skill1,
    @JsonProperty("criticalChance")
    Integer skill2) implements Serializable {
}
