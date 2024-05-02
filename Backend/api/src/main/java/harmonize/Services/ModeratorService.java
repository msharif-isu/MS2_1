package harmonize.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.DTOs.ResponseDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;

@Service
public class ModeratorService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserService userService;

    @Autowired
    public ModeratorService(UserRepository userRepository, RoleRepository roleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
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
        return getUser(id, true);
    }

    @NonNull
    public UserDTO getUser(int id, boolean roleChecking) {
        User user = userRepository.findReferenceById(id);

        if (user == null || (roleChecking &&
            (!user.getRoles().contains(roleRepository.findByName("USER")) && 
             !user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            ))
            throw new EntityNotFoundException("User " + id + " not found.");
            
        return new UserDTO(user);
    }

    @NonNull
    public UserDTO getUser(String username) {
        return getUser(username, true);
    }

    @NonNull
    public UserDTO getUser(String username, boolean roleChecking) {
        User user = userRepository.findByUsername(username);

        if (user == null || (roleChecking &&
            (!user.getRoles().contains(roleRepository.findByName("USER")) && 
             !user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            ))
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

            
        userService.deleteUser(id);
        
        return new ResponseDTO(String.format("\"%s\" was deleted.", user.getUsername()));
    }

    public byte[] getIcon(int id) {
        User user = userRepository.findReferenceById(id);

        if (user == null ||
            (!user.getRoles().contains(roleRepository.findByName("USER")) && 
             !user.getRoles().contains(roleRepository.findByName("MODERATOR")))
            )
            throw new EntityNotFoundException("User " + id + " not found.");

        try {
            File file = new File(user.getIcon().getPath());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new InternalServerErrorException("Error: Was unable to read image.");
        }
    }
}
