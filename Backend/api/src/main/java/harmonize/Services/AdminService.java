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
import harmonize.Entities.Role;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityAlreadyExistsException;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;

@Service
public class AdminService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdminService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    
    @NonNull
    public List<UserDTO> getAllUsers() {
        List<UserDTO> result = new ArrayList<UserDTO>();
        for (User user : userRepository.findAll()) {
            result.add(new UserDTO(user));
        }
        return result;
    }
    
    @NonNull
    public UserDTO getUser(int id) {
        User user = userRepository.findReferenceById(id);

        if (user == null)
            throw new EntityNotFoundException("User " + id + " not found.");
            
        return new UserDTO(user);
    }

    @NonNull
    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null)
            throw new EntityNotFoundException("User " + username + " not found.");
            
        return new UserDTO(user);
    }

    @NonNull
    public ResponseDTO updateRole(int id, String role){
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if(newRole == null)
            throw new EntityNotFoundException("Role " + role + " not found.");

        if(user.getRoles().contains(newRole))
            throw new EntityAlreadyExistsException("User " + id + " already has " + role + " role.");

        user.getRoles().add(newRole);
        userRepository.save(user);

        return new ResponseDTO(String.format("\"%s\" has been added to \"%s\"", role, user.getUsername()));
    }

    @NonNull
    public ResponseDTO deleteRole(int id, String role) {
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if(newRole == null)
            throw new EntityNotFoundException("Role " + role + " not found.");

        if(!user.getRoles().contains(newRole))
            throw new EntityNotFoundException("User " + id + " did not have " + role + " role.");

        user.getRoles().remove(newRole);
        userRepository.save(user);

        return new ResponseDTO(String.format("\"%s\" has been deleted from \"%s\"", role, user.getUsername()));
    }

    public byte[] getIcon(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        try {
            File file = new File(user.getIcon().getPath());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new InternalServerErrorException("Error: Was unable to read image.");
        }
    }
}
