package harmonize.Entities.FeedItems;

import java.util.Date;
import java.util.Objects;

import harmonize.Entities.Post;
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
@DiscriminatorValue(value = "post")
public class PostFeedItem extends AbstractFeedItem {
    public static final long POST_EXPIRATION_DATE_MS = 86400 * 1000 * 30;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "id")    
    private Post post;

    public PostFeedItem() {
        super();
    }

    public PostFeedItem(FeedEnum type, Post post, User user) {
        super(new Date(System.currentTimeMillis() + POST_EXPIRATION_DATE_MS), 
            type, user);
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostFeedItem feedItem = (PostFeedItem) o;
        return Objects.equals(this.getUser(), feedItem.getUser()) && Objects.equals(post, feedItem.getPost());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getUser(), post);
    }
}
