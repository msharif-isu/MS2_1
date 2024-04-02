package harmonize.Entities.FeedItems;

import java.util.Objects;

import harmonize.Entities.Song;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "song_id", referencedColumnName = "id")    
    private Song song;

    public SongFeedItem() {
        super();
    }

    public SongFeedItem(FeedEnum type, Song song) {
        super(type);
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongFeedItem feedItem = (SongFeedItem) o;
        return Objects.equals(getType(), feedItem.getType()) && Objects.equals(song, feedItem.song);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), song);
    }
}
