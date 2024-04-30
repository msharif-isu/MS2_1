package harmonize.Entities.FeedItems;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import harmonize.Entities.User;
import harmonize.Enum.FeedEnum;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "feed_item_type",
    discriminatorType = DiscriminatorType.STRING
)
@Table(name = "feed_items")
@AllArgsConstructor
@Data
public abstract class AbstractFeedItem {
    public static final long DEFAULT_EXPIRATION_DATE_MS = 86400 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FeedEnum type;

    @Column(name = "expiration")
    private Date expiration;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") 
    private User user;

    public AbstractFeedItem() {
        expiration = new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_DATE_MS);
    }

    public AbstractFeedItem(Date expiration, FeedEnum type, User user) {
        this.user = user;
        this.type = type;
        this.expiration = expiration;
    }

    @PreRemove
    public void removeReference() {
        if (this.user != null)
            this.user.getSeenFeed().remove(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AbstractFeedItem feedItem = (AbstractFeedItem) obj;
        return this.id == feedItem.getId();
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
}   
