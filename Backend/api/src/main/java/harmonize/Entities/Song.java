package harmonize.Entities;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    private String id;

    private String title;

    private String artist;

    public Song(JsonNode song) {
        this.id = song.get("id").asText();
        this.title = song.get("name").asText();
        this.artist = song.get("artists").get(0).get("name").asText();
    }
}
