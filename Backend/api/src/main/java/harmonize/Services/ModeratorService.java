package harmonize.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.DTOs.ResponseDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;

@Service
public class ModeratorService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ModeratorService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @NonNull
    public List<UserDTO> getAllUsers() {
        List<UserDTO> result = new ArrayList<UserDTO>();
        for (User user : userRepository.findAllByRole("USER")) {
            result.add(new UserDTO(user));
        }
        for (User user : userRepository.findAllByRole("MODERATOR")) {
            result.add(new UserDTO(user));
        }
        return result;
    }
    
    @NonNull
    public UserDTO getUser(int id) {
        User user = userRepository.findReferenceById(id);

        if (user == null ||
            (!user.getRoles().contains(roleRepository.findByName("USER")) && 
             !user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            )
            throw new EntityNotFoundException("User " + id + " not found.");
            
        return new UserDTO(user);
    }

    @NonNull
    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null ||
            (!user.getRoles().contains(roleRepository.findByName("USER")) && 
             !user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            )
            throw new EntityNotFoundException("User " + username + " not found.");
            
        return new UserDTO(user);
    }

    @NonNull
    public ResponseDTO deleteUser(int id){
        User user = userRepository.findReferenceById(id);

        if (user == null ||
            (!user.getRoles().contains(roleRepository.findByName("USER")) && 
             !user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            )
            throw new EntityNotFoundException("User " + id + " not found.");
        if (user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            throw new UnauthorizedException("Moderators may not delete other moderators.");

            
        userRepository.delete(user);
        
        return new ResponseDTO(String.format("\"%s\" was deleted.", user.getUsername()));
    }
}
