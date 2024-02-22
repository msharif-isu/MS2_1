package harmonize.Controllers;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.Roles.Role;
import harmonize.Roles.RoleRepository;
import harmonize.Users.User;
import harmonize.Users.UserRepository;
import io.jsonwebtoken.lang.Collections;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserRepository userRepository;
    
    private RoleRepository roleRepository;

    @Autowired
    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        if(userRepository.findReferenceById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userRepository.findReferenceById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/users/{id}/{username}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @PathVariable String username){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);

        if(userRepository.findByUsername(username) != null)
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
            
        userRepository.setUsername(id, username);
        
        return new ResponseEntity<>("\"" + user.getUsername() + "\"" + " was updated to " + "\"" + username + "\"", HttpStatus.OK);
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        User deletedUser = userRepository.findReferenceById(id);

        deletedUser.getRoles().removeAll(deletedUser.getRoles());
        userRepository.deleteById(id);
        
        return new ResponseEntity<>("\"" + deletedUser.getUsername() + "\"" + " has been deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/roles/{id}/{role}")
    public ResponseEntity<String> updateRole(@PathVariable int id, @PathVariable String role){
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

    @DeleteMapping(path = "/roles/{id}/{role}")
    public ResponseEntity<String> deleteRole(@PathVariable int id, @PathVariable String role){
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);

        if(newRole == null)
            return new ResponseEntity<>("Role does not exist", HttpStatus.NOT_FOUND);

        if(!user.getRoles().contains(newRole))
            return new ResponseEntity<>("User does not have role", HttpStatus.NOT_FOUND);

        user.getRoles().remove(newRole);
        userRepository.save(user);

        return new ResponseEntity<>("\"" + role + "\"" + " has been deleted from " + "\"" + user.getUsername() + "\"", HttpStatus.OK);
    }

}
