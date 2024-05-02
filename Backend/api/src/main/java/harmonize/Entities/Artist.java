package harmonize.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
    @Column(unique = true)
    private String id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="artist", fetch = FetchType.EAGER)
    private Set<Song> songs = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="artist", fetch = FetchType.LAZY)
    private Set<Album> albums = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REMOVE }, 
                orphanRemoval = true)
    @OrderBy("frequency ASC")
    private List<ArtistFreq> topListeners = new ArrayList<>();

    public Artist(JsonNode artist) {
        this.id = artist.get("id").asText();
        this.name = artist.get("name").asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return this.id.equals(artist.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}