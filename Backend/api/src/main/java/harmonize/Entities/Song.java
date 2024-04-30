package harmonize.Entities;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="artist_id", referencedColumnName = "id")
    private Artist artist;

    public Song(JsonNode song) {
        this.id = song.get("id").asText();
        this.title = song.get("name").asText();
        this.artist = new Artist(song.get("artists").get(0));
    }

    public Song(JsonNode song, Artist artist) {
        this.id = song.get("id").asText();
        this.title = song.get("name").asText();
        this.artist = artist;
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
