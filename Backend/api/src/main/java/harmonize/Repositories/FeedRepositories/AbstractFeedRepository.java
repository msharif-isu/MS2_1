package harmonize.Repositories.FeedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import harmonize.Entities.FeedItems.AbstractFeedItem;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@NoRepositoryBean
public interface AbstractFeedRepository extends JpaRepository<AbstractFeedItem, Long> {

}
