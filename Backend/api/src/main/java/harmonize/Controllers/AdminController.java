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
import harmonize.Services.AdminService;
import harmonize.Services.UserService;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/admin")
public class AdminController {   
    private AdminService adminService;
    private UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping(path = "/")
    public ResponseEntity<UserDTO> getSelf(Principal principal){
        return ResponseEntity.ok(adminService.getUser(adminService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id){
        return ResponseEntity.ok(adminService.getUser(id));
    }

    @GetMapping(path = "/friends/recommended/{id}")
    public ResponseEntity<List<UserDTO>> getRecommendedFriends(@PathVariable int id) {
        return ResponseEntity.ok(userService.getRecommendedFriends(id));
    }

    @GetMapping(path = "/friends/{id}")
    public ResponseEntity<List<UserDTO>> getFriends(@PathVariable int id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @GetMapping(path = "/friends/invites/{id}")
    public ResponseEntity<List<UserDTO>> getFriendInvites(@PathVariable int id) {
        return ResponseEntity.ok(userService.getFriendInvites(id));
    }

    @PostMapping(path = "/friends/{id1}/{id2}")
    public ResponseEntity<String> addFriend(@PathVariable int id1, @PathVariable int id2) {
        return ResponseEntity.ok(userService.addFriend(id1, id2));
    }

    @DeleteMapping(path = "/friends/{id1}/{id2}")
    public ResponseEntity<String> removeFriend(@PathVariable int id1, @PathVariable int id2) {
        return ResponseEntity.ok(userService.removeFriend(id1, id2));
    }
 
    @PutMapping(path = "/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UserDTO update) {
        return ResponseEntity.ok(userService.updateUser(id, update));
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping(path = "/roles/{id}/{role}")
    public ResponseEntity<String> updateRole(@PathVariable int id, @PathVariable String role) {
        return ResponseEntity.ok(adminService.updateRole(id, role));
    }

    @DeleteMapping(path = "/roles/{id}/{role}")
    public ResponseEntity<String> deleteRole(@PathVariable int id, @PathVariable String role) {
        return ResponseEntity.ok(adminService.deleteRole(id, role));
    }
}
