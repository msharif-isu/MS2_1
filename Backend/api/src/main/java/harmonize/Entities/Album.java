package harmonize.Entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "albums")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    @Id
    @Column(unique = true)
    private String id;

    private String name;

    private String imageUrl;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="artist_id", referencedColumnName = "id")
    private Artist artist;

    @JsonIgnore
    @OneToMany(mappedBy="album", fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();

    public Album(JsonNode album, Artist artist) {
        this.id = album.get("id").asText();
        this.name = album.get("name").asText();
        this.imageUrl = album.get("images").get(0).get("url").asText();
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return this.id.equals(album.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
