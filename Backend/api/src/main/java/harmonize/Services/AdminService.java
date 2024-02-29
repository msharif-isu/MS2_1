package harmonize.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.DTOs.UserDTO;
import harmonize.ErrorHandling.Exceptions.RoleNotFoundException;
import harmonize.ErrorHandling.Exceptions.RolePermissionException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyFriendException;
import harmonize.ErrorHandling.Exceptions.UserFriendSelfException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UserNotFriendException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Roles.Role;
import harmonize.Roles.RoleRepository;
import harmonize.Users.User;
import harmonize.Users.UserRepository;

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
            result.add(new UserDTO(user.getId(), user.getUsername()));
        }
        return result;
    }
    
    @NonNull
    public UserDTO getUser(int id) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new UserNotFoundException(id);
        return new UserDTO(user.getId(), user.getUsername());
    }

    @NonNull
    public List<UserDTO> getFriends(int id) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new UserNotFoundException(id);

        List<UserDTO> result = new ArrayList<UserDTO>();
        for (User friend : user.getFriends()) {
            result.add(new UserDTO(friend.getId(), friend.getUsername()));
        }

        return result;
    }

    @NonNull
    public List<UserDTO> getFriendInvites(int id) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new UserNotFoundException(id);

        List<UserDTO> result = new ArrayList<UserDTO>();
        for (User friendInviter : user.getFriendInvites()) {
            result.add(new UserDTO(friendInviter.getId(), friendInviter.getUsername()));
        }

        return result;
    }

    @NonNull
    public List<UserDTO> getRecommendedFriends(int id) {
        List<UserDTO> result = new ArrayList<UserDTO>();
        User currUser = userRepository.findReferenceById(id);

        userRepository.findAllByRole("USER").forEach(user -> {
            if(user.getId() == id)
                return;
            if (currUser.getFriends().contains(user))
                return;
            if (user.getFriendInvites().contains(currUser))
                return;
            
            result.add(new UserDTO(user.getId(), user.getUsername()));
        });

        return result;
    }

    @NonNull
    public String addFriends(int id1, int id2) {
        User user1 = userRepository.findReferenceById(id1);
        User user2 = userRepository.findReferenceById(id2);
        
        if (user1 == null)
            throw new UserNotFoundException(id1);
        if (user2 == null)
            throw new UserNotFoundException(id2);
        if (user1 == user2)
            throw new UserFriendSelfException(user1.getUsername());

        if (user1.getFriends().contains(user2))
            throw new UserAlreadyFriendException(user1.getUsername(), user2.getUsername());

        if (user1.getFriendInvites().contains(user2))
            user1.getFriendInvites().remove(user2);
        
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
        userRepository.save(user1);
        userRepository.save(user2);
        return new String(String.format("\"%s\" and \"%s\" are now friends", user1.getUsername(), user2.getUsername()));
    }

    @NonNull
    public String removeFriends(int id1, int id2) {
        User user1 = userRepository.findReferenceById(id1);
        User user2 = userRepository.findReferenceById(id2);

        if (user1 == null)
            throw new UserNotFoundException(id1);
        if (user2 == null)
            throw new UserNotFoundException(id2);

        if (user1.getFriendInvites().contains(user2)) {
            user1.getFriendInvites().remove(user2);
            userRepository.save(user1);
            return new String(String.format("\"%s\" removed friend invite to \"%s\"", user1.getUsername(), user2.getUsername()));
        }

        if (user2.getFriendInvites().contains(user1)) {
            user2.getFriendInvites().remove(user1);
            userRepository.save(user2);
            return new String(String.format("\"%s\" removed friend invite from \"%s\"", user1.getUsername(), user2.getUsername()));
        }

        if (!user1.getFriends().contains(user2))
            throw new UserNotFriendException(user1.getUsername(), user2.getUsername());

        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);
        userRepository.save(user1);
        userRepository.save(user2);
        return new String(String.format("\"%s\" is no longer friends with \"%s\"", user1.getUsername(), user2.getUsername()));
    }

    @NonNull
    public String updateUser(int id, String username) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        if(userRepository.findByUsername(username) != null)
            throw new UsernameTakenException(username);
            
        userRepository.setUsername(id, username);
        
        return new String(String.format("\"%s\" was updated to \"%s\"", user.getUsername(), username));
    }

    @NonNull
    public String deleteUser(int id){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        user.getRoles().removeAll(user.getRoles());
        userRepository.deleteById(id);
        
        return new String(String.format("\"%s\" has been deleted", user.getUsername()));
    }

    @NonNull
    public String updateRole(int id, String role){
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            throw new UserNotFoundException(id);

        if(newRole == null)
            throw new RoleNotFoundException(role);

        if(user.getRoles().contains(newRole))
            throw new RolePermissionException(role);

        user.getRoles().add(newRole);
        userRepository.save(user);

        return new String(String.format("\"%s\" has been added to \"%s\"", role, user.getUsername()));
    }

    @NonNull
    public String deleteRole(int id, String role) {
        User user = userRepository.findReferenceById(id);
        Role newRole = roleRepository.findByName(role);

        if(user == null)
            throw new UserNotFoundException(id);

        if(newRole == null)
            throw new RoleNotFoundException(role);

        if(!user.getRoles().contains(newRole))
            throw new RolePermissionException(role);

        user.getRoles().remove(newRole);
        userRepository.save(user);

        return new String(String.format("\"%s\" has been deleted from \"%s\"", role, user.getUsername()));
    }
}
