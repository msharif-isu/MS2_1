package harmonize.Entities.FeedItems;

import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.Enum.FeedEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "song")
public class SongFeedItem extends AbstractFeedItem {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "song_id", referencedColumnName = "id")    
    private Song song;

    public SongFeedItem() {
        super();
    }

    public SongFeedItem(FeedEnum type, Song song, User user) {
        super(type, user);
        this.song = song;
    }
}
