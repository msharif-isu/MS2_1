package harmonize.Entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy="artist", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Song> songs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "artist_users", 
               joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> listeners = new HashSet<>();

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