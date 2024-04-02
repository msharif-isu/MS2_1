package harmonize.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.SongDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Entities.Role;
import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.Entities.LikedSong;
import harmonize.ErrorHandling.Exceptions.EntityAlreadyExistsException;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyFriendException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyInvitedException;
import harmonize.ErrorHandling.Exceptions.UserFriendSelfException;
import harmonize.ErrorHandling.Exceptions.UserInfoInvalidException;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.ErrorHandling.Exceptions.UserNotFriendException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.SongRepository;
import harmonize.Repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SongRepository songRepository;

    private ConversationService conversationService;
    private MusicService musicService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, SongRepository songRepository,
                        ConversationService conversationService, MusicService musicService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.songRepository = songRepository;
        this.conversationService = conversationService;
        this.musicService = musicService;
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
    public UserDTO updateUser(int id, UserDTO update){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        if (update.getUsername().isEmpty())
            throw new UserInfoInvalidException("Username cannot be empty.");

        if(userRepository.findByUsername(update.getUsername()) != null && userRepository.findByUsername(update.getUsername()) != user)
            throw new UsernameTakenException(update.getUsername());

        if(update.getFirstName() != null)
            user.setFirstName(update.getFirstName());
            
        if(update.getLastName() != null)
            user.setLastName(update.getLastName());

        if(update.getUsername() != null)
            user.setUsername(update.getUsername());

        if(update.getBio() != null)
            user.setBio(update.getBio());

        userRepository.save(user);
        
        return new UserDTO(user);
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
    public List<RoleDTO> getRoles(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        List<RoleDTO> roles = new ArrayList<RoleDTO>();

        for (Role role : user.getRoles())
            roles.add(new RoleDTO(role));

        return roles;
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
            if (user.getFriends().contains(currUser))
                continue;
            if (currUser.getFriendInvites().contains(user))
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
            throw new UserNotFoundException(id);
            
        if (user == friend)
            throw new UserFriendSelfException(user.getUsername());

        if (user.getFriends().contains(friend))
            throw new UserAlreadyFriendException(user.getUsername(), friend.getUsername());
        
        if (friend.getFriendInvites().contains(user))
            throw new UserAlreadyInvitedException(user.getUsername(), friend.getUsername());

        if (!user.getFriendInvites().contains(friend)) {
            friend.getFriendInvites().add(user);
            userRepository.save(friend);
            return new String(String.format("\"%s\" sent friend invite to \"%s\"", user.getUsername(), friend.getUsername()));
        }

        conversationService.createConversation(Set.of(user, friend));

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

        conversationService.deleteConversation(Set.of(user, friend));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(friend);
        userRepository.save(user);
        return new String(String.format("\"%s\" is no longer friends with \"%s\"", user.getUsername(), friend.getUsername()));
    }

    public List<SongDTO> getSongs(int id) {
        User user = userRepository.findReferenceById(id);

        if (user == null)
            throw new UserNotFoundException(id);

        List<SongDTO> songList = new ArrayList<>();

        for(LikedSong element : user.getLikedSongs())
            songList.add(new SongDTO(element));

        return songList;
    }

    public String addSong(int id, String songId) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        Song song = new Song(musicService.getSong(songId));

        LikedSong connection = new LikedSong(user, song);

        if(user.getLikedSongs().contains(connection))
            throw new EntityAlreadyExistsException(song.getTitle() + " already added.");

        user.getLikedSongs().add(connection);
        userRepository.save(user);
        updateTopArtist(user);

        return new String(String.format("\"%s\" favorited \"%s\"", user.getUsername(), song.getTitle()));
    }

    public String removeSong(int id, String songId) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new UserNotFoundException(id);

        Song song = new Song(musicService.getSong(songId));

        LikedSong connection = new LikedSong(user, song);

        if(!user.getLikedSongs().contains(connection))
            throw new EntityNotFoundException(song.getTitle() + " could not be found.");

        user.getLikedSongs().remove(connection);
        userRepository.save(user);
        updateTopArtist(user);
        
        return new String(String.format("\"%s\" removed \"%s\"", user.getUsername(), song.getTitle()));
    }

    private void updateTopArtist(User user) {
        List<String> topArtists = songRepository.findTopArtists(user.getLikedSongs());

        user.getTopArtists().clear();

        for(int i = 0; i < topArtists.size(); i++)
            user.getTopArtists().add(topArtists.get(i));

        userRepository.save(user);
    }
}
