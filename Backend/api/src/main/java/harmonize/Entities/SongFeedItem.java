package harmonize.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue(value = "song")
public class SongFeedItem extends FeedItem {
    @Column(name = "song_type")
    private String songType;

    @ManyToOne
    @JoinColumn(name = "song_id")    
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
        if (o == null || this.getClass() != o.getClass()) return false;
        SongFeedItem song = (SongFeedItem) o;
        return this.getId() == song.getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
