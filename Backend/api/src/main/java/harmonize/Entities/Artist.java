package harmonize.Entities;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artists")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    @Id
    private String id;

    private String name;

    public Artist(JsonNode artist) {
        this.id = artist.get("id").asText();
        this.name = artist.get("name").asText();
    }
}
