package harmonize.Services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import harmonize.Users.User;
import harmonize.Users.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseEntity<User> getUserById(int id) {
        if(userRepository.findReferenceById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userRepository.findReferenceById(id), HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByUsername(String username) {
        if(userRepository.findByUsername(username) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userRepository.findByUsername(username), HttpStatus.OK);
    }

    public ResponseEntity<String> updateUser(int id, String username, Principal principal){
        User user = userRepository.findReferenceById(id);

        if(user.getId() != userRepository.findByUsername(principal.getName()).getId())
            return new ResponseEntity<>("Cannot change another user", HttpStatus.BAD_REQUEST);

        if(userRepository.findByUsername(username) != null)
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
            
        userRepository.setUsername(id, username);
        
        return new ResponseEntity<>("\"" + user.getUsername() + "\"" + " was updated to " + "\"" + username + "\"", HttpStatus.OK);
    }
}
