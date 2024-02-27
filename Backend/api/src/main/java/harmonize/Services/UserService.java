package harmonize.Services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.DTOs.UserDTO;
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
    public List<UserDTO> getPossibleFriends(Principal principal) {
        User currUser = userRepository.findByUsername(principal.getName());

        List<UserDTO> users = new ArrayList<UserDTO>();

        userRepository.findAllByRole("USER").forEach(user -> {
            if (user.getId() == currUser.getId())
                return;
            
            users.add(new UserDTO(user.getId(), user.getUsername()));
        });

        return users;
    }

    @NonNull
    public UserDTO getUserById(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        return new UserDTO(user.getId(), user.getUsername());
    }

    @NonNull
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        
        if(user == null)
            throw new UserNotFoundException(username);

        return new UserDTO(user.getId(), user.getUsername());
    }

    @NonNull
    public String updateUsername(String username, Principal principal){
        User user = userRepository.findByUsername(principal.getName());

        if(userRepository.findByUsername(username) != null)
            throw new UsernameTakenException(username);
            
        userRepository.setUsername(user.getId(), username);
        
        return new String(String.format("\"%s\" was updated to \"%s\"", user.getUsername(), username));
    }
}
