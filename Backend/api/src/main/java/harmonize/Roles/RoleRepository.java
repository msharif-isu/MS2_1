package harmonize.Roles;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
