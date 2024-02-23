package harmonize.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Modifying
    void deleteById(int id);
    
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.username = ?2 where u.id = ?1")
    void setUsername(int userId, String username);
}
