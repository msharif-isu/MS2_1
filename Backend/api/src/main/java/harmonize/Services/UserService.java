package harmonize.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.DTOs.UserDTO;
import harmonize.ErrorHandling.Exceptions.UserAlreadyFriendException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyInvitedException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UserNotFriendException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Roles.RoleRepository;
import harmonize.Users.User;
import harmonize.Users.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @NonNull
    public UserDTO getUser(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null || !user.getRoles().contains(roleRepository.findByName("USER")))
            throw new UserNotFoundException(id);

        return new UserDTO(user);
    }

    @NonNull
    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username);
        
        if(user == null || !user.getRoles().contains(roleRepository.findByName("USER")))
            throw new UserNotFoundException(username);

        return new UserDTO(user);
    }

    @NonNull
    public String updateUser(int id, UserDTO update){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        if(userRepository.findByUsername(update.getUsername()) != null)
            throw new UsernameTakenException(update.getUsername());

        String nullUpdate = "Nothing was changed for user: " + id;
        String response = "";

        if(update.getFirstName() != null) {
            response += new String(String.format("First name: \"%s\" was updated to \"%s.\"\n", user.getFirstName(), update.getFirstName()));
            user.setFirstName(update.getFirstName());
        }
            
        if(update.getLastName() != null) {
            response += new String(String.format("Last name: \"%s\" was updated to \"%s.\"\n", user.getLastName(), update.getLastName()));
            user.setLastName(update.getLastName());
        }

        if(update.getUsername() != null) {
            response += new String(String.format("Username: \"%s\" was updated to \"%s.\"\n", user.getUsername(), update.getUsername()));
            user.setUsername(update.getUsername());
        }

        if(update.getBio() != null) {
            response += new String(String.format("Bio: \"%s\" was updated to \"%s.\"\n", user.getBio(), update.getBio()));
            user.setBio(update.getBio());
        }

        userRepository.save(user);
        response = response.trim();
        
        return response.isEmpty() ? nullUpdate : response;
    }

    @NonNull
    public String deleteUser(int id){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);
            
        userRepository.delete(user);
        
        return new String(String.format("\"%s\" was deleted.", user.getUsername()));
    }

    @NonNull
    public List<UserDTO> getRecommendedFriends(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        List<UserDTO> recommendedFriends = new ArrayList<UserDTO>();

        for (User currUser : userRepository.findAllByRole("USER")) {
            if (currUser.getId() == user.getId())
                continue;

            recommendedFriends.add(new UserDTO(currUser));
        }

        return recommendedFriends;
    }

    @NonNull
    public List<UserDTO> getFriends(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        List<UserDTO> friends = new ArrayList<UserDTO>();

        for (User friend : user.getFriends())
            friends.add(new UserDTO(friend));

        return friends;
    }

    @NonNull
    public List<UserDTO> getFriendInvites(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        List<UserDTO> inviters = new ArrayList<UserDTO>();

        for (User inviter : user.getFriendInvites())
            inviters.add(new UserDTO(inviter));

        return inviters;
    }

    @NonNull
    public String addFriend(int id, int idFriend) {
        User user = userRepository.findReferenceById(id);
        User friend = userRepository.findReferenceById(idFriend);

        if(user == null)
            throw new UserNotFoundException(id);

        if (friend == null)
            throw new UserNotFoundException(idFriend);

        if (user.getFriends().contains(friend))
            throw new UserAlreadyFriendException(user.getUsername(), friend.getUsername());
        
        if (friend.getFriendInvites().contains(user))
            throw new UserAlreadyInvitedException(user.getUsername(), friend.getUsername());

        if (!user.getFriendInvites().contains(friend)) {
            friend.getFriendInvites().add(user);
            userRepository.save(friend);
            return new String(String.format("\"%s\" sent friend invite to \"%s\"", user.getUsername(), friend.getUsername()));
        }

        user.getFriendInvites().remove(friend);
        friend.getFriends().add(user);
        user.getFriends().add(friend);
        userRepository.save(friend);
        userRepository.save(user);
        return new String(String.format("\"%s\" and \"%s\" are now friends", user.getUsername(), friend.getUsername()));
    
    }

    @NonNull
    public String removeFriend(int id, int idFriend) {
        User user = userRepository.findReferenceById(id);
        User friend = userRepository.findReferenceById(idFriend);

        if (user == null)
            throw new UserNotFoundException(id);

        if (friend == null)
            throw new UserNotFoundException(idFriend);

        if (friend.getFriendInvites().contains(user)) {
            friend.getFriendInvites().remove(user);
            userRepository.save(friend);
            return new String(String.format("\"%s\" removed friend invite to \"%s\"", user.getUsername(), friend.getUsername()));
        }

        if (user.getFriendInvites().contains(friend)) {
            user.getFriendInvites().remove(friend);
            userRepository.save(user);
            return new String(String.format("\"%s\" removed friend invite from \"%s\"", user.getUsername(), friend.getUsername()));
        }

        if (!user.getFriends().contains(friend))
            throw new UserNotFriendException(user.getUsername(), friend.getUsername());

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(friend);
        userRepository.save(user);
        return new String(String.format("\"%s\" is no longer friends with \"%s\"", user.getUsername(), friend.getUsername()));
    }

}
