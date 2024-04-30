package harmonize.Repositories.FeedRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import harmonize.Entities.FeedItems.AbstractFeedItem;
import jakarta.transaction.Transactional;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@NoRepositoryBean
public interface FeedRepository extends JpaRepository<AbstractFeedItem, Long> {
    @Transactional
    @Modifying
    @Query("SELECT f FROM AbstractFeedItem f WHERE f.expiration < CURRENT_TIMESTAMP")
    List<AbstractFeedItem> getExpiredFeedItems();
}
