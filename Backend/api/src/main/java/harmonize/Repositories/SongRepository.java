package harmonize.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Song;
import harmonize.Entities.UserSong;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Song findReferenceById(String id);

    @Query("SELECT l.song.artistId " +
           "FROM UserSong l " +
           "WHERE l IN :likedSongs " +
           "GROUP BY l.song.artistId " +
           "ORDER BY COUNT(l.song.artistId) DESC " +
           "LIMIT 10")
    List<String> findTopArtists(List<UserSong> likedSongs);
}
