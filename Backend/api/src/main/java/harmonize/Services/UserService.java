package harmonize.Services;

import java.security.Principal;
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
    public User getSelf(Principal principal) {
        return userRepository.findByUsername(principal.getName());
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

    @NonNull
    public List<UserDTO> getRecommendedFriends(Principal principal) {
        User currUser = userRepository.findByUsername(principal.getName());

        List<UserDTO> recommendedFriends = new ArrayList<UserDTO>();

        userRepository.findAllByRole("USER").forEach(user -> {
            if (user.getId() == currUser.getId())
                return;
            
            recommendedFriends.add(new UserDTO(user.getId(), user.getUsername()));
        });

        return recommendedFriends;
    }

    @NonNull
    public List<UserDTO> getFriends(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        List<UserDTO> friends = new ArrayList<UserDTO>();

        for (User friend : user.getFriends())
            friends.add(new UserDTO(friend.getId(), friend.getUsername()));

        return friends;
    }

    @NonNull
    public List<UserDTO> getFriendInvites(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        List<UserDTO> inviters = new ArrayList<UserDTO>();

        for (User inviter : user.getFriendInvites())
            inviters.add(new UserDTO(inviter.getId(), inviter.getUsername()));

        return inviters;
    }

    @NonNull
    public String addFriend(int id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        User friend = userRepository.findReferenceById(id);

        if (friend == null)
            throw new UserNotFoundException(id);

        if (user.getFriends().contains(friend))
            throw new UserAlreadyFriendException(friend.getUsername());
        
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
    public String removeFriend(int id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        User friend = userRepository.findReferenceById(id);

        if (friend == null)
            throw new UserNotFoundException(id);

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
