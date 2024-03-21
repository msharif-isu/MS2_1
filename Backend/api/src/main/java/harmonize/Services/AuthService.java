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
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.UserInfoInvalidException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;
import harmonize.Security.ChatCrypto;
import harmonize.Security.TokenGenerator;
import harmonize.Security.ChatCrypto.Keys;

@Service
public class AuthService {
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder;

    private TokenGenerator tokenGenerator;

    private ChatCrypto chatCrypto;

    @Autowired
    public AuthService (UserRepository userRepository, RoleRepository roleRepository, 
                        AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder, TokenGenerator tokenGenerator, ChatCrypto chatCrypto) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.tokenGenerator = tokenGenerator;
        this.chatCrypto = chatCrypto;
    }
    
    @NonNull
    public AuthDTO login(LoginDTO user) {    
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenGenerator.generateToken(authentication);

        return new AuthDTO(token);
    }

    @NonNull
    public AuthDTO register(RegisterDTO user) {
        if (user.getUsername().isEmpty())
            throw new UserInfoInvalidException("Username cannot be empty.");
        if (userRepository.findByUsername(user.getUsername()) != null)
            throw new UsernameTakenException(user.getUsername());
        
        User newUser = new User(user.getFirstName(), user.getLastName(), user.getUsername(), encoder.encode(user.getPassword()));
        try {
            Keys key = chatCrypto.generateKeyPair();
            newUser.setPublicKey(key.getPublicKey());
            newUser.setPrivateKeyWrapped(chatCrypto.wrap(user.getPassword(), key.getPrivateKey()));
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        newUser.setRoles(Collections.singleton(roleRepository.findByName("USER")));
        
        userRepository.save(newUser);

        LoginDTO newUserLogin = new LoginDTO(user.getUsername(), user.getPassword());

        return login(newUserLogin);
    }
}
