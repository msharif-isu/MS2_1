package harmonize.Controllers;

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

import harmonize.Users.User;
import harmonize.Users.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
