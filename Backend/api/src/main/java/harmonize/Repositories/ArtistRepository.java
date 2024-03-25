package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Artist;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

 @Repository
 public interface ArtistRepository extends JpaRepository<Artist, Long> {
     Artist findReferenceById(String id);
 }
 
