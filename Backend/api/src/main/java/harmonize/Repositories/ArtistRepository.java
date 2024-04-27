package harmonize.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Artist;
import harmonize.Entities.LikedSong;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    Artist findReferenceById(String id);

    @NonNull
    List<Artist> findAll();

    @Query("SELECT l.song.artist FROM LikedSong l " +
            "WHERE l IN :likedSongs " +
            "GROUP BY l.song.artist " +
            "ORDER BY COUNT(l.song.artist) DESC")
    List<Artist> findTopArtists(List<LikedSong> likedSongs);
}
