package harmonize.Controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping(path = "/username/{username}")
    public ResponseEntity<User> getIdByUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping(path = "/{id}/{username}")
    public ResponseEntity<String> updateUsername(@PathVariable int id, @PathVariable String username, Principal principal){
        return ResponseEntity.ok(userService.updateUsername(id, username, principal));
    }
}

