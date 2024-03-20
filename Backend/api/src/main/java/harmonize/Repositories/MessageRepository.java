package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Message;

/**
 * 
 * @author Isaac Denning
 * 
 */ 

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findReferenceById(int id);
}
 