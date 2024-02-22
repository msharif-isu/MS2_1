package harmonize.Services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.Roles.RoleRepository;
import harmonize.Security.TokenGenerator;
import harmonize.Users.User;
import harmonize.Users.UserRepository;

@Service
public class AuthService {
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder;

    private TokenGenerator tokenGenerator;

    @Autowired
    public AuthService (UserRepository userRepository, RoleRepository roleRepository, 
                            AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.tokenGenerator = tokenGenerator;
    }
    
    public ResponseEntity<AuthDTO> login(LoginDTO user) {    
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenGenerator.generateToken(authentication);

        return new ResponseEntity<>(new AuthDTO(token), HttpStatus.OK);
    }

    public ResponseEntity<String> registerUser(RegisterDTO user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            return new ResponseEntity<>("Username Taken", HttpStatus.CONFLICT);
        
        User newUser = new User(user.getUsername(), encoder.encode(user.getPassword()));

        newUser.setRoles(Collections.singleton(roleRepository.findByName("USER")));

        userRepository.save(newUser);
        return new ResponseEntity<>("Registered User", HttpStatus.CREATED);
    }
}
