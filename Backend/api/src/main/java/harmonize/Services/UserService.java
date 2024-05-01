package harmonize.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.SongDTO;
import harmonize.DTOs.UserDTO;
import harmonize.DTOs.FriendRecDTO;
import harmonize.Entities.Role;
import harmonize.Entities.Song;
import harmonize.Entities.User;
import harmonize.Entities.Image;
import harmonize.Entities.Conversation;
import harmonize.Entities.ArtistFreq;
import harmonize.Entities.LikedSong;
import harmonize.ErrorHandling.Exceptions.EntityAlreadyExistsException;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InternalServerErrorException;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.ErrorHandling.Exceptions.UserNotFriendException;
import harmonize.Repositories.ConversationRepository;
import harmonize.Repositories.ArtistFreqRepository;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.SongRepository;
import harmonize.Repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ConversationRepository conversationRepository;
    private SongRepository songRepository;
    private ArtistFreqRepository artistFreqRepository;

    private ConversationService conversationService;
    private MusicService musicService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, ConversationRepository conversationRepository, ArtistFreqRepository artistFreqRepository,
                        ConversationService conversationService, MusicService musicService, SongRepository songRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.conversationRepository = conversationRepository;
        this.artistFreqRepository = artistFreqRepository;
        this.conversationService = conversationService;
        this.musicService = musicService;
        this.songRepository = songRepository;
    }

    @NonNull
    public UserDTO getUser(int id) {
        return getUser(id, true);
    }

    @NonNull
    public UserDTO getUser(int id, boolean roleChecking) {
        User user = userRepository.findReferenceById(id);

        if(user == null || (roleChecking && !user.getRoles().contains(roleRepository.findByName("USER"))))
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
        
        if(user == null || (roleChecking && !user.getRoles().contains(roleRepository.findByName("USER"))))
            throw new EntityNotFoundException("User " + username + " not found.");

        return new UserDTO(user);
    }

    @NonNull
    public UserDTO updateUser(int id, UserDTO update){
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if (update.getUsername().isEmpty())
            throw new InvalidArgumentException("Username cannot be empty.");

        if(userRepository.findByUsername(update.getUsername()) != null && userRepository.findByUsername(update.getUsername()) != user)
            throw new EntityAlreadyExistsException("Username" + user.getUsername() + " already taken.");

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
            throw new EntityNotFoundException("User " + id + " not found.");

        deleteIcon(id);

        for (User other : userRepository.findAll()) {
            if (other.getFriendInvites().contains(user)) {
                other.getFriendInvites().remove(user);
                userRepository.save(other);
            }
        }

        for (User friend : user.getFriends()) {
            friend.getFriends().remove(user);
            userRepository.save(friend);
        }
        
        List<Conversation> conversationCopy = new ArrayList<>(user.getConversations());
        conversationCopy.stream().forEach(conversation -> conversationService.removeMember(conversation, user));
            
        userRepository.delete(user);
        
        return new String(String.format("\"%s\" was deleted.", user.getUsername()));
    }

    @NonNull 
    public List<RoleDTO> getRoles(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        List<RoleDTO> roles = new ArrayList<RoleDTO>();

        for (Role role : user.getRoles())
            roles.add(new RoleDTO(role));

        return roles;
    }

    @NonNull
    @Transactional
    public Set<FriendRecDTO> getRecommendedFriends(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        Set<FriendRecDTO> recommendedFriends = new HashSet<>();
        List<ArtistFreq> topArtists = user.getTopArtists();
        Random random = new Random();

        for(int i = 0; i < topArtists.size() && i < 10; i++) {
            List<ArtistFreq> topListeners = topArtists.get(i).getArtist().getTopListeners();
            Hibernate.initialize(topListeners);
            
            for (int j = 0; j < 5; j++) {
                ArtistFreq connection = topListeners.get(random.nextInt(topListeners.size()));
                User connectionUser = connection.getUser();
                FriendRecDTO pair = new FriendRecDTO(connectionUser, connection.getArtist());

                if(connectionUser.getId() == user.getId())
                    continue;

                if(user.getFriends().contains(connectionUser))
                    continue;

                if(!connectionUser.getRoles().contains(roleRepository.findByName("USER")))
                    continue;

                if(connectionUser.getFriendInvites().contains(user) || user.getFriendInvites().contains(connectionUser))
                    continue;

                recommendedFriends.add(pair);
            }
        }

        return recommendedFriends;
    }

    @NonNull
    public List<UserDTO> getFriends(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        List<UserDTO> friends = new ArrayList<UserDTO>();

        for (User friend : user.getFriends())
            friends.add(new UserDTO(friend));

        return friends;
    }

    @NonNull
    public List<UserDTO> getFriendInvites(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

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
            throw new EntityNotFoundException("User " + id + " not found.");
        if (friend == null)
            throw new EntityNotFoundException("User " + id + " not found.");
            
        if (user == friend)
            throw new InvalidArgumentException("User cannot friend self.");

        if (user.getFriends().contains(friend))
            throw new EntityAlreadyExistsException("User " + user.getUsername() + " already friends with " + friend.getUsername() + ".");
        
        if (friend.getFriendInvites().contains(user))
            throw new EntityAlreadyExistsException("User " + user.getUsername() + " already sent friend invite to " + friend.getUsername() + ".");

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
            throw new EntityNotFoundException("User " + id + " not found.");

        if (friend == null)
            throw new EntityNotFoundException("User " + id + " not found.");

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
            throw new EntityNotFoundException("User " + id + " not found.");

        List<SongDTO> songList = new ArrayList<>();

        for(LikedSong element : user.getLikedSongs())
            songList.add(new SongDTO(element));

        return songList;
    }

    public String addSong(int id, String songId) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        Song song = songRepository.findReferenceById(musicService.getSong(songId).getId());

        LikedSong connection = new LikedSong(user, song);

        if(user.getLikedSongs().contains(connection))
            throw new EntityAlreadyExistsException(song.getTitle() + " already added.");

        user.getLikedSongs().add(connection);
        updateTopArtists(user, song, true);
        userRepository.save(user);

        return new String(String.format("\"%s\" favorited \"%s\"", user.getUsername(), song.getTitle()));
    }

    public String removeSong(int id, String songId) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        Song song = songRepository.findReferenceById(musicService.getSong(songId).getId());

        LikedSong connection = new LikedSong(user, song);

        if(!user.getLikedSongs().contains(connection))
            throw new EntityNotFoundException(song.getTitle() + " could not be found.");

        user.getLikedSongs().remove(connection);
        updateTopArtists(user, song, false);
        userRepository.save(user);
        
        return new String(String.format("\"%s\" removed \"%s\"", user.getUsername(), song.getTitle()));
    }

    private void updateTopArtists(User user, Song song, Boolean add) {
        User managedUser = userRepository.findReferenceById(user.getId());
        List<ArtistFreq> userArtist = managedUser.getTopArtists();
        ArtistFreq connection = new ArtistFreq(user, song.getArtist());
    
        if(userArtist.contains(connection)) {
            int index = userArtist.indexOf(connection);
            ArtistFreq managedConnection = userArtist.get(index);

            managedConnection.setFrequency(managedConnection.getFrequency() + (add ? 1 : -1));
            
            if(managedConnection.getFrequency() == 0) {
                userArtist.remove(managedConnection);
                artistFreqRepository.delete(managedConnection);
            }
        }
        else
            userArtist.add(connection);
        
        userRepository.save(managedUser);
    }

    public ConversationDTO createConversation(int id, List<Integer> memberIds) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        Set<User> members = new HashSet<>() {};
        members.add(user);
        for (int memberId : memberIds) {
            User other = userRepository.findReferenceById(memberId);
            if(user.getId() != other.getId() && user.getFriends().stream().noneMatch(friend -> friend.getId() == other.getId()))
                throw new EntityNotFoundException("User " + memberId + " was not found in user " + id + " friend list.");
            members.add(other);
        }

        return new ConversationDTO(conversationService.createConversation(members));
    }

    public String leaveConversation(int id, int convoId) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        Conversation conversation = conversationRepository.findReferenceById(convoId);
        if(conversation == null)
            throw new EntityNotFoundException("Cannot find conversation with id: " + convoId);


        conversationService.removeMember(conversation, user);

        return "Memeber " + id + " removed from conversation " + convoId + ".";
    }

    public byte[] getIcon(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null || !user.getRoles().contains(roleRepository.findByName("USER")))
            throw new EntityNotFoundException("User " + id + " not found.");

        if (user.getIcon() == null)
            throw new EntityNotFoundException("User " + id + " does not have and icon.");

        try {
            File file = new File(user.getIcon().getPath());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new InternalServerErrorException("Error: Was unable to read image.");
        }
    }

    public byte[] saveIcon(int id, MultipartFile imageFile) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        try {
            if (user.getIcon() != null) {
                File file = new File(user.getIcon().getPath());
                if (file.exists())
                    file.delete();
            }

            File file = new File(System.getProperty("user.dir") + File.separator + "icons" + File.separator + user.getId() + '-' + imageFile.getOriginalFilename());
            imageFile.transferTo(file); 
            
            user.setIcon(new Image(file.getAbsolutePath()));
            userRepository.save(user);

            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new InternalServerErrorException("Error: Was unable to save image.");
        }
    }

    public String deleteIcon(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if (user.getIcon() == null)
            return "User " + id + " did not have and icon.";

        if (user.getIcon() != null) {
            File file = new File(user.getIcon().getPath());
            if (file.exists()) {
                file.delete();
                user.setIcon(null);
                userRepository.save(user);
                return "Icon for user " + id + " deleted.";
            } else {
                throw new InternalServerErrorException("Image file for user " + id + " not found.");
            }
        } else {
            throw new EntityNotFoundException("Icon for user " + id + " not found.");
        }
    }
}
