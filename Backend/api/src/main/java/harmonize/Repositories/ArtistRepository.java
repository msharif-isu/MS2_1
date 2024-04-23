package harmonize.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Artist;

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
}
