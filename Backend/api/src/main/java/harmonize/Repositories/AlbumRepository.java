package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Album;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
    Album findReferenceById(String id);
}
