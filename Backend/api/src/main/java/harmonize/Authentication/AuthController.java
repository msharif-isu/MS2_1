package harmonize.Authentication;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.Roles.RoleRepository;
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
    private UserController userController;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public AuthController (UserController userController, UserRepository userRepository, RoleRepository roleRepository, 
                            AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder) {
        this.userController = userController;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }
    
    @PostMapping(path = "/auth/login")
    public ResponseEntity<String> ValidLogin(@RequestBody LoginDTO user) {    
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User Signed In", HttpStatus.OK);
    }

    @PostMapping(path = "/auth/register")
    public String RegisterUser(@RequestBody RegisterDTO user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            return "Username taken";
        
        User newUser = new User(user.getUsername(), encoder.encode(user.getPassword()));

        newUser.setRoles(Collections.singletonList(roleRepository.findByName("USER")));

        return userController.createUser(newUser);
    }
}
