package harmonize.Entities;

import java.util.Date;

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
@Table(name = "liked_songs")
@Data   
@AllArgsConstructor
@NoArgsConstructor
public class LikedSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "time")
    private Date time;

    public LikedSong(User user, Song song) {
        this.user = user;
        this.song = song;
        this.time = new Date(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikedSong likedSong = (LikedSong) o;
        return this.user.getId() == likedSong.user.getId() && this.song.getId().hashCode() == likedSong.song.getId().hashCode();
    }
    
    @Override
    public int hashCode() {
        return 31 * this.user.getId() + this.song.getId().hashCode();
    }
}
