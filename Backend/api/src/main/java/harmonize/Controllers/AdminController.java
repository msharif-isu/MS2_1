package harmonize.Controllers;

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
import harmonize.Services.AdminService;
import harmonize.Users.User;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/admin")
public class AdminController {   
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id){
        return ResponseEntity.ok(adminService.getUser(id));
    }

    @GetMapping(path = "/friends/{id}")
    public ResponseEntity<List<User>> getRecommendedFriends(@PathVariable int id) {
        return ResponseEntity.ok(adminService.getRecommendedFriends(id));
    }

    @PostMapping(path = "/friend/{id1}/{id2}")
    public ResponseEntity<String> addFriends(@PathVariable int id1, @PathVariable int id2) {
        return ResponseEntity.ok(adminService.addFriends(id1, id2));
    }

    @DeleteMapping(path = "/friend/{id1}/{id2}")
    public ResponseEntity<String> removeFriends(@PathVariable int id1, @PathVariable int id2) {
        return ResponseEntity.ok(adminService.removeFriends(id1, id2));
    }
 
    @PutMapping(path = "/users/{id}/{username}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @PathVariable String username) {
        return ResponseEntity.ok(adminService.updateUser(id, username));
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
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
