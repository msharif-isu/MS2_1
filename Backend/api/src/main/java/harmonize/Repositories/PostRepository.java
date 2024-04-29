package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Post;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

 @Repository
 public interface PostRepository extends JpaRepository<Post, Long> {
     Post findReferenceById(int id);
 }