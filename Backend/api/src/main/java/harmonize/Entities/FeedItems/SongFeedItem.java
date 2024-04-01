package harmonize.Entities.FeedItems;

import java.util.Objects;

import harmonize.Entities.Song;
import jakarta.persistence.Column;
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
    @Column(name = "song_type")
    private String songType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "song_id", referencedColumnName = "id")    
    private Song song;

    public SongFeedItem() {
        super();
    }

    public SongFeedItem(String songType, Song song) {
        super();
        this.songType = songType;
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongFeedItem feedItem = (SongFeedItem) o;
        return Objects.equals(songType, feedItem.songType) && Objects.equals(song, feedItem.song);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songType, song);
    }
}
