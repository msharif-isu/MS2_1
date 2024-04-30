package harmonize.Entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artist_frequency")
@Data   
@AllArgsConstructor
@NoArgsConstructor
public class ArtistFreq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Column(name = "frequency")
    private int frequency;

    public ArtistFreq(User user, Artist artist) {
        this.user = user;
        this.artist = artist;
        this.frequency = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistFreq artistFreq = (ArtistFreq) o;
        return Objects.equals(user, artistFreq.user) && Objects.equals(artist, artistFreq.artist);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(user, artist);
    }
}
