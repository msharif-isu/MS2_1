package harmonize.Authentication;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private BCryptPasswordEncoder encoder;

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public AuthController (BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }
    
    @PostMapping(path = "/auth/login")
    public boolean ValidLogin(@Valid @RequestBody User user) {      
        User validUser = userRepository.findByUsername(user.getUsername());  

        if(validUser == null)
            return false;

        return encoder.matches(user.getPassword(), validUser.getPassword());
    }

    @PostMapping(path = "/auth/register")
    public String RegisterUser(@Valid @RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            return "Username taken";
        
        User encodedUser = new User(user.getUsername(), encoder.encode(user.getPassword()));

        return userController.createUser(encodedUser);
    }
}
