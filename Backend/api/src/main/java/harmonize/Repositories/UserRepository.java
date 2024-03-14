package harmonize.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import harmonize.Entities.User;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 

@Repository
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

    @Query("select u from User u left join u.roles role WHERE role.name = ?1")
    List<User> findAllByRole(String roleName);
}
