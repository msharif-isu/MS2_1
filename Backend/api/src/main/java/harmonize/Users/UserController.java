package harmonize.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Isaac Denning
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id){
        return userRepository.findReferenceById(id);
    }

    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user){
        if (user == null)
            return failure;

        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findReferenceById(id);
        if(user == null || request == null)
            return null;
            
        userRepository.save(request);
        return userRepository.findReferenceById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    public String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
}

