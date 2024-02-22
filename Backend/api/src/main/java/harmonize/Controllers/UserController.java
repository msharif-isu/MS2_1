package harmonize.Controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.Users.User;
import harmonize.Users.UserRepository;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/users/{id}")
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        return new ResponseEntity<>(userRepository.findReferenceById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/{username}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @PathVariable String username, Principal principal){
        User user = userRepository.findReferenceById(id);

        if(user.getId() != userRepository.findByUsername(principal.getName()).getId())
            return new ResponseEntity<>("Cannot change another user", HttpStatus.BAD_REQUEST);

        if(userRepository.findByUsername(username) != null)
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
            
        userRepository.setUsername(id, username);
        
        return new ResponseEntity<>("\"" + user.getUsername() + "\"" + " was updated to " + "\"" + username + "\"", HttpStatus.OK);
    }
}

