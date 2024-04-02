package harmonize.Entities;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
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
    @Column(unique = true)
    private String id;

    private String artistId;

    private String title;

    private String artist;

    public Song(JsonNode song) {
        this.id = song.get("id").asText();
        this.title = song.get("name").asText();
        this.artistId = song.get("artists").get(0).get("id").asText();
        this.artist = song.get("artists").get(0).get("name").asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return this.id.equals(song.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
