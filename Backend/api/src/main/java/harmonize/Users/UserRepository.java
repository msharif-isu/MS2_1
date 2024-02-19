package harmonize.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findReferenceById(int id);

    @Transactional
    void deleteById(int id);

    @Query("select u from User u where u.username = ?1 and u.password = ?2")
    User findByUsernameAndPassword(String username, String password);
    
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);
}
