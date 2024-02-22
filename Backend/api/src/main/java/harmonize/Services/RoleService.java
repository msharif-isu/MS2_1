package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import harmonize.Roles.Role;
import harmonize.Roles.RoleRepository;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<String> CreateRole(String role) {
        Role newRole = new Role(role);

        roleRepository.save(newRole);

        return new ResponseEntity<>(newRole.getName() + " has been made", HttpStatus.OK);
    }
}
