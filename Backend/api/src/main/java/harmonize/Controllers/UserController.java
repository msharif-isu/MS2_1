package harmonize.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping(path = "/friends")
    public ResponseEntity<List<UserDTO>> getPossibleFriends(Principal principal){
        return ResponseEntity.ok(userService.getPossibleFriends(principal));
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
}

