package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Chat;

/**
 * 
 * @author Isaac Denning
 * 
 */ 

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findReferenceById(int id);
}
 