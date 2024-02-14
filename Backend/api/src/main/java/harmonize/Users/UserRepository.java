package harmonize.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Isaac Denning
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findReferenceById(int id);

    @Transactional
    void deleteById(int id);
}
