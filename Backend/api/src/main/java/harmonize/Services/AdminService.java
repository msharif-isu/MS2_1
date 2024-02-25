package harmonize.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.ErrorHandling.Exceptions.RoleNotFoundException;
import harmonize.ErrorHandling.Exceptions.RolePermissionException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Roles.Role;
import harmonize.Roles.RoleRepository;
import harmonize.Users.User;
import harmonize.Users.UserRepository;

@Service
public class AdminService {
    private UserRepository userRepository;
    
    private RoleRepository roleRepository;

    @Autowired
    public AdminService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    
    @NonNull
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @NonNull
    public List<User> getRecommendedFriends(int id) {
        List<User> possibleFriends = new ArrayList<User>();
        userRepository.findAllByRole("USER").forEach(user -> {
            if(user.getId() == id)
                return;
            
            possibleFriends.add(user);
        });

        return possibleFriends;
    }

    @NonNull
    public String updateUser(int id, String username) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        if(userRepository.findByUsername(username) != null)
            throw new UsernameTakenException(username);
            
        userRepository.setUsername(id, username);
        
        return new String(String.format("\"%s\" was updated to \"%s\"", user.getUsername(), username));
    }

    @NonNull
    public String deleteUser(int id){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        user.getRoles().removeAll(user.getRoles());
        userRepository.deleteById(id);
        
        return new String(String.format("\"%s\" has been deleted", user.getUsername()));
    }

    @NonNull
    public String updateRole(int id, String role){
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            throw new UserNotFoundException(id);

        if(newRole == null)
            throw new RoleNotFoundException(role);

        if(user.getRoles().contains(newRole))
            throw new RolePermissionException(role);

        user.getRoles().add(newRole);
        userRepository.save(user);

        return new String(String.format("\"%s\" has been added to \"%s\"", role, user.getUsername()));
    }

    @NonNull
    public String deleteRole(int id, String role) {
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            throw new UserNotFoundException(id);

        if(newRole == null)
            throw new RoleNotFoundException(role);

        if(!user.getRoles().contains(newRole))
            throw new RolePermissionException(role);

        user.getRoles().remove(newRole);
        userRepository.save(user);

        return new String(String.format("\"%s\" has been deleted from \"%s\"", role, user.getUsername()));
    }
}
