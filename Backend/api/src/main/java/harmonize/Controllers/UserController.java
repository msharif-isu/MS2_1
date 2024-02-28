package harmonize.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.DTOs.UserDTO;
import harmonize.Services.UserService;
import harmonize.Users.User;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<User> getSelf(Principal principal){
        return ResponseEntity.ok(userService.getSelf(principal));
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping(path = "/username/{username}")
    public ResponseEntity<UserDTO> getIdByUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping(path = "/username/{username}")
    public ResponseEntity<String> updateUsername(@PathVariable String username, Principal principal){
        return ResponseEntity.ok(userService.updateUsername(username, principal));
    }

    @GetMapping(path = "/friends")
    public ResponseEntity<List<UserDTO>> getFriends(Principal principal){
        return ResponseEntity.ok(userService.getFriends(principal));
    }

    @GetMapping(path = "/friends/recommended")
    public ResponseEntity<List<UserDTO>> getRecommendedFriends(Principal principal){
        return ResponseEntity.ok(userService.getRecommendedFriends(principal));
    }

    @GetMapping(path = "/friends/invites")
    public ResponseEntity<List<UserDTO>> getFriendInvites(Principal principal){
        return ResponseEntity.ok(userService.getFriendInvites(principal));
    }

    @PostMapping(path = "/friends/add/{id}")
    public ResponseEntity<String> addFriend(@PathVariable int id, Principal principal){
        return ResponseEntity.ok(userService.addFriend(id, principal));
    }

    @DeleteMapping(path = "/friends/remove/{id}")
    public ResponseEntity<String> removeFriend(@PathVariable int id, Principal principal){
        return ResponseEntity.ok(userService.removeFriend(id, principal));
    }
}

