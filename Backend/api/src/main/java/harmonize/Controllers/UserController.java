package harmonize.Controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.FriendRecDTO;
import harmonize.DTOs.PostDTO;
import harmonize.DTOs.ReportDTO;
import harmonize.DTOs.ResponseDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.SongDTO;
import harmonize.DTOs.UserDTO;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.Services.PostService;
import harmonize.Services.ReportService;
import harmonize.Services.UserService;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private ReportService reportService;
    private PostService postService;

    @Autowired
    public UserController(UserService userService, ReportService reportService, PostService postService) {
        this.userService = userService;
        this.reportService = reportService;
        this.postService = postService;
    }

    /**
     * Get all users
     * @param principal
     * @return
     */
    @GetMapping(path = "")
    public ResponseEntity<UserDTO> getSelf(Principal principal){
        return ResponseEntity.ok(userService.getUser(userService.getUser(principal.getName(), false).getId(), false));
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping(path = "/username/{username}")
    public ResponseEntity<UserDTO> getIdByUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PutMapping(path = "")
    public ResponseEntity<UserDTO> updateUser(Principal principal, @RequestBody UserDTO update){
        return ResponseEntity.ok(userService.updateUser(userService.getUser(principal.getName(), false).getId(), update));
    }

    @DeleteMapping(path = "")
    public ResponseEntity<ResponseDTO> deleteUser(Principal principal){
        return ResponseEntity.ok(userService.deleteUser(userService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/roles")
    public ResponseEntity<List<RoleDTO>> getRoles(Principal principal){
        return ResponseEntity.ok(userService.getRoles(userService.getUser(principal.getName(), false).getId()));
    }

    @GetMapping(path = "/friends")
    public ResponseEntity<List<UserDTO>> getFriends(Principal principal){
        return ResponseEntity.ok(userService.getFriends(userService.getUser(principal.getName(), false).getId()));
    }

    @GetMapping(path = "/friends/recommended")
    public ResponseEntity<Set<FriendRecDTO>> getRecommendedFriends(Principal principal){
        return ResponseEntity.ok(userService.getRecommendedFriends(userService.getUser(principal.getName(), false).getId()));
    }

    @GetMapping(path = "/friends/invites")
    public ResponseEntity<List<UserDTO>> getFriendInvites(Principal principal){
        return ResponseEntity.ok(userService.getFriendInvites(userService.getUser(principal.getName(), false).getId()));
    }

    @PostMapping(path = "/friends/{id}")
    public ResponseEntity<ResponseDTO> addFriend(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(userService.addFriend(userService.getUser(principal.getName()).getId(), id));
    }

    @DeleteMapping(path = "/friends/{id}")
    public ResponseEntity<ResponseDTO> removeFriend(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(userService.removeFriend(userService.getUser(principal.getName()).getId(), id));
    }

    @GetMapping(path = "/reports")
    public ResponseEntity<List<ReportDTO>> getSentReports(Principal principal){
        return ResponseEntity.ok(reportService.getSentReports(userService.getUser(principal.getName(), false).getId()));
    }

    @GetMapping(path = "/reports/{id}")
    public ResponseEntity<ReportDTO> getSentReport(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(reportService.getSentReport(userService.getUser(principal.getName(), false).getId(), id));
    }

    @PostMapping(path = "/reports")
    public ResponseEntity<ReportDTO> sendReport(Principal principal, @RequestBody ReportDTO report){
        return ResponseEntity.ok(reportService.sendReport(userService.getUser(principal.getName(), false).getId(), report));
    }

    @DeleteMapping(path = "/reports/{id}")
    public ResponseEntity<ResponseDTO> deleteSentReport(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(reportService.deleteSentReport(userService.getUser(principal.getName()).getId(), id));
    }

    @GetMapping(path = "/songs")
    public ResponseEntity<List<SongDTO>> getSongs(Principal principal){
        return ResponseEntity.ok(userService.getSongs(userService.getUser(principal.getName(), false).getId()));
    }

    @PostMapping(path = "/songs/{id}")
    public ResponseEntity<ResponseDTO> addSong(Principal principal, @PathVariable String id){
        return ResponseEntity.ok(userService.addSong(userService.getUser(principal.getName()).getId(), id));
    }

    @DeleteMapping(path = "/songs/{id}")
    public ResponseEntity<ResponseDTO> removeSong(Principal principal, @PathVariable String id){
        return ResponseEntity.ok(userService.removeSong(userService.getUser(principal.getName()).getId(), id));
    }

    @GetMapping(path = "/icons", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getIcon(Principal principal){
        return ResponseEntity.ok(userService.getIcon(userService.getUser(principal.getName(), false).getId()));
    }

    @GetMapping(path = "/icons/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getIcon(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(userService.getIcon(id));
    }

    @PostMapping(path = "/icons", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> saveIcon(Principal principal, @RequestParam("image") MultipartFile image){
        return ResponseEntity.ok(userService.saveIcon(userService.getUser(principal.getName(), false).getId(), image));
    }

    @DeleteMapping(path = "/icons")
    public ResponseEntity<String> deleteIcon(Principal principal){
        return ResponseEntity.ok(userService.deleteIcon(userService.getUser(principal.getName(), false).getId()));
    }
    
    @PostMapping(path = "/conversations")
    public ResponseEntity<ConversationDTO> createConversation(Principal principal, @RequestBody JsonNode body){
        if (!body.has("memberIds"))
            throw new InvalidArgumentException("No \"memberIds\" field found.");

        if (!body.get("memberIds").isArray())
            throw new InvalidArgumentException("The \"memberIds\" field must be a list.");

        List<Integer> memberIds = StreamSupport.stream(body.get("memberIds").spliterator(), false)
            .filter(JsonNode::isInt)
            .map(JsonNode::asInt)
            .collect(Collectors.toList());

        return ResponseEntity.ok(userService.createConversation(userService.getUser(principal.getName(), false).getId(), memberIds));
    }

    @DeleteMapping(path = "/conversations/{id}")
    public ResponseEntity<String> leaveConversation(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(userService.leaveConversation(userService.getUser(principal.getName(), false).getId(), id));
    }

    @GetMapping(path = "/posts")
    public ResponseEntity<List<PostDTO>> getPosts(Principal principal){
        return ResponseEntity.ok(postService.getPosts(userService.getUser(principal.getName(), false).getId()));
    }

    @GetMapping(path = "/posts/{id}")
    public ResponseEntity<List<PostDTO>> getPosts(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(postService.getPosts(userService.getUser(principal.getName(), false).getId(), id));
    }

    @PostMapping(path = "/posts")
    public ResponseEntity<PostDTO> sendPost(Principal principal, @RequestBody PostDTO post){
        return ResponseEntity.ok(postService.sendPost(userService.getUser(principal.getName(), false).getId(), post.getPost()));
    }

    @DeleteMapping(path = "/posts/{id}")
    public ResponseEntity<PostDTO> deleteSentPost(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(postService.deleteSentPost(userService.getUser(principal.getName(), false).getId(), id));
    }
}

