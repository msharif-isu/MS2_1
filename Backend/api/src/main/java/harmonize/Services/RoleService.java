package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

    @NonNull
    public String CreateRole(String role) {
        Role newRole = new Role(role);

        roleRepository.save(newRole);

        return new String(String.format("\"%s\" has been created", newRole.getName()));
    }
}
