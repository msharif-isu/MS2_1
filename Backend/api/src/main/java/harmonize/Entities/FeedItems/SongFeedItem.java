package harmonize.Entities.FeedItems;

import java.util.Date;
import java.util.Objects;

import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.Enum.FeedEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue(value = "song")
public class SongFeedItem extends AbstractFeedItem {
    public static final long SONG_EXPIRATION_DATE_MS = 86400 * 1000;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", referencedColumnName = "id")    
    private Song song;

    public SongFeedItem() {
        super();
    }

    public SongFeedItem(FeedEnum type, Song song, User user) {
        super(new Date(System.currentTimeMillis() + SONG_EXPIRATION_DATE_MS), 
                type, user);
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongFeedItem feedItem = (SongFeedItem) o;
        return Objects.equals(this.getUser(), feedItem.getUser()) && Objects.equals(song, feedItem.getSong());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getUser(), song);
    }
}
