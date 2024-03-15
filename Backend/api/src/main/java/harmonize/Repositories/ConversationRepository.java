package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Conversation;

/**
 * 
 * @author Isaac Denning
 * 
 */ 

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findReferenceById(int id);
}
 