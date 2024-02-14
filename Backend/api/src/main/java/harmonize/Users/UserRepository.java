package harmonize.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select u from User u where u.username = ?1 and u.password = ?2")
    User findByUsernameandPassword(String username, String password);
}
