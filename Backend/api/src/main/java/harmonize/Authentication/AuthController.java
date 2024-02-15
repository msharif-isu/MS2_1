package harmonize.Authentication;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import harmonize.Users.User;
import harmonize.Users.UserController;
import harmonize.Users.UserRepository;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@RestController
public class AuthController {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    
    @PostMapping(path = "/auth/login")
    public boolean ValidLogin(@RequestBody User user) {        
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()) != null;
    }

    @PostMapping(path = "/auth/register")
    public String RegisterUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            return "Username taken";   
        
        return userController.createUser(user);
    }
}
