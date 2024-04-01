package harmonize.Entities.FeedItems;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@Table(name = "seen_feed_items")
@AllArgsConstructor
@Data
public abstract class AbstractFeedItem {
    public static final long ITEM_EXPIRATION_DATE_MS = 86400 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "expiration")
    private Date expiration;

    public AbstractFeedItem() {
        expiration = new Date(System.currentTimeMillis() + ITEM_EXPIRATION_DATE_MS);
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
