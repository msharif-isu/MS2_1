package harmonize.Services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.DTOs.UserDTO;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
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
    
    @NonNull
    public AuthDTO login(LoginDTO user) {    
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenGenerator.generateToken(authentication);

        return new AuthDTO(token);
    }

    @NonNull
    public UserDTO registerUser(RegisterDTO user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            throw new UsernameTakenException(user.getUsername());
        
        User newUser = new User(user.getUsername(), encoder.encode(user.getPassword()));

        newUser.setRoles(Collections.singleton(roleRepository.findByName("USER")));

        userRepository.save(newUser);
        return new UserDTO(newUser.getId(), newUser.getUsername());
    }
}
