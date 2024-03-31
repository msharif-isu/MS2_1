package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.FeedItem;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Repository
public interface FeedRepository extends JpaRepository<FeedItem, Long> {
    FeedItem findReferenceById(int id);
}
