package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Song;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    Song findReferenceById(String id);
}
