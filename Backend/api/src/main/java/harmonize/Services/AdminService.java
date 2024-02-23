package harmonize.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
    public String updateUser(int id, String username) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        if(userRepository.findByUsername(username) != null)
            throw new UsernameTakenException(username);
            
        userRepository.setUsername(id, username);
        
        return "\"" + user.getUsername() + "\"" + " was updated to " + "\"" + username + "\"";
    }

    public ResponseEntity<String> deleteUser(int id){
        User deletedUser = userRepository.findReferenceById(id);

        deletedUser.getRoles().removeAll(deletedUser.getRoles());
        userRepository.deleteById(id);
        
        return new ResponseEntity<>("\"" + deletedUser.getUsername() + "\"" + " has been deleted", HttpStatus.OK);
    }

    public ResponseEntity<String> updateRole(int id, String role){
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);

        if(newRole == null)
            return new ResponseEntity<>("Role does not exist", HttpStatus.NOT_FOUND);

        if(user.getRoles().contains(newRole))
            return new ResponseEntity<>("User already has role", HttpStatus.NOT_FOUND);

        user.getRoles().add(newRole);
        userRepository.save(user);

        return new ResponseEntity<>("\"" + role + "\"" + " has been added to " + "\"" + user.getUsername() + "\"", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteRole(int id, String role) {
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);

        if(role == null)
            return new ResponseEntity<>("Role does not exist", HttpStatus.NOT_FOUND);

        if(!user.getRoles().contains(newRole))
            return new ResponseEntity<>("User does not have role", HttpStatus.NOT_FOUND);

        user.getRoles().remove(newRole);
        userRepository.save(user);

        return new ResponseEntity<>("\"" + role + "\"" + " has been deleted from " + "\"" + user.getUsername() + "\"", HttpStatus.OK);
    }
}
