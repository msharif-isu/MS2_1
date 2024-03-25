package harmonize.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Song;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Song findReferenceById(String id);

    // @Query("SELECT DISTINCT ls.artist " +
    //        "FROM User u " +
    //        "JOIN u.likedSongs ls " +
    //        "WHERE u.id = :userId " +
    //        "GROUP BY ls.artist " +
    //        "ORDER BY COUNT(ls) DESC")
    // List<String> findMostFrequentArtistsByUserId(@Param("userId") int userId);
}
