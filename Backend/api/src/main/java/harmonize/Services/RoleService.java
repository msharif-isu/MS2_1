package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;
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

    public String CreateRole(String role) {
        Role newRole = new Role(role);

        roleRepository.save(newRole);

        return newRole.getName() + " has been made";
    }
}
