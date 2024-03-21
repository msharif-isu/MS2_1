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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.DTOs.UserDTO;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "")
    public ResponseEntity<UserDTO> getSelf(Principal principal){
        return ResponseEntity.ok(userService.getUser(userService.getUser(principal.getName()).getId()));
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
        return ResponseEntity.ok(userService.updateUser(userService.getUser(principal.getName()).getId(), update));
    }

    @DeleteMapping(path = "")
    public ResponseEntity<String> deleteUser(Principal principal){
        return ResponseEntity.ok(userService.deleteUser(userService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/friends")
    public ResponseEntity<List<UserDTO>> getFriends(Principal principal){
        return ResponseEntity.ok(userService.getFriends(userService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/friends/recommended")
    public ResponseEntity<List<UserDTO>> getRecommendedFriends(Principal principal){
        return ResponseEntity.ok(userService.getRecommendedFriends(userService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/friends/invites")
    public ResponseEntity<List<UserDTO>> getFriendInvites(Principal principal){
        return ResponseEntity.ok(userService.getFriendInvites(userService.getUser(principal.getName()).getId()));
    }

    @PostMapping(path = "/friends/{id}")
    public ResponseEntity<String> addFriend(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(userService.addFriend(userService.getUser(principal.getName()).getId(), id));
    }

    @DeleteMapping(path = "/friends/{id}")
    public ResponseEntity<String> removeFriend(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(userService.removeFriend(userService.getUser(principal.getName()).getId(), id));
    }
}

