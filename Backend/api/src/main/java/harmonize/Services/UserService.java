package harmonize.Services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.ErrorHandling.Exceptions.UnauthorizedUserException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Users.User;
import harmonize.Users.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    public User getUserById(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        return user;
    }

    @NonNull
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        
        if(user == null)
            throw new UserNotFoundException(username);

        return user;
    }

    @NonNull
    public String updateUsername(int id, String username, Principal principal){
        User user = userRepository.findReferenceById(id);

        if(user.getId() != userRepository.findByUsername(principal.getName()).getId())
            throw new UnauthorizedUserException();

        if(userRepository.findByUsername(username) != null)
            throw new UsernameTakenException(username);
            
        userRepository.setUsername(id, username);
        
        return new String(String.format("\"%s\" was updated to \"%s\"", user.getUsername(), username));
    }
}
