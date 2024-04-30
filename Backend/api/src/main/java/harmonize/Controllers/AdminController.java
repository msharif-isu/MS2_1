package harmonize.Controllers;

import java.security.Principal;
import java.util.List;

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

import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.FriendRecDTO;
import harmonize.DTOs.PostDTO;
import harmonize.DTOs.ReportDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Services.AdminService;
import harmonize.Services.MessageService;
import harmonize.Services.PostService;
import harmonize.Services.ReportService;
import harmonize.Services.UserService;

/**
 * 
 * @author Phu Nguyen and Isaac Denning
 * 
 */ 

@RestController
@RequestMapping("/admins")
public class AdminController {   
    private AdminService adminService;
    private UserService userService;
    private ReportService reportService;
    private MessageService messageService;
    private PostService postService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, ReportService reportService, MessageService messageService, PostService postService) {
        this.adminService = adminService;
        this.userService = userService;
        this.reportService = reportService;
        this.messageService = messageService;
        this.postService = postService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping(path = "")
    public ResponseEntity<UserDTO> getSelf(Principal principal){
        return ResponseEntity.ok(adminService.getUser(principal.getName()));
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id){
        return ResponseEntity.ok(adminService.getUser(id));
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO update){
        return ResponseEntity.ok(userService.updateUser(id, update));
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping(path = "/users/icons", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getIcon(Principal principal){
        return ResponseEntity.ok(adminService.getIcon(adminService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/users/icons/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getIcon(@PathVariable int id){
        return ResponseEntity.ok(adminService.getIcon(id));
    }

    @PostMapping(path = "/users/icons/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> saveIcon(@RequestParam("image") MultipartFile image, @PathVariable int id){
        return ResponseEntity.ok(userService.saveIcon(id, image));
    }

    @DeleteMapping(path = "/users/icons/{id}")
    public ResponseEntity<String> deleteIcon(@PathVariable int id){
        return ResponseEntity.ok(userService.deleteIcon(id));
    }

    @PostMapping(path = "/users/conversations/{id}")
    public ResponseEntity<ConversationDTO> createConversation(@PathVariable int id, @RequestBody List<Integer> others){
        return ResponseEntity.ok(userService.createConversation(id, others));
    }

    @DeleteMapping(path = "/users/conversations/{id}/{convoId}")
    public ResponseEntity<String> removeConversation(@PathVariable int id, @PathVariable int convoId){
        return ResponseEntity.ok(userService.leaveConversation(id, convoId));
    }

    @GetMapping(path = "/friends/recommended/{id}")
    public ResponseEntity<List<FriendRecDTO>> getRecommendedFriends(@PathVariable int id) {
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

    @GetMapping(path = "/roles/{id}")
    public ResponseEntity<List<RoleDTO>> getRoles(@PathVariable int id){
        return ResponseEntity.ok(userService.getRoles(id));
    }

    @PutMapping(path = "/roles/{id}/{role}")
    public ResponseEntity<String> updateRole(@PathVariable int id, @PathVariable String role) {
        return ResponseEntity.ok(adminService.updateRole(id, role));
    }

    @DeleteMapping(path = "/roles/{id}/{role}")
    public ResponseEntity<String> deleteRole(@PathVariable int id, @PathVariable String role) {
        return ResponseEntity.ok(adminService.deleteRole(id, role));
    }

    @GetMapping(path = "/reports")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getReports());
    }

    @GetMapping(path = "/reports/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable int id) {
        return ResponseEntity.ok(reportService.getReport(id));
    }

    @GetMapping(path = "/reports/sent/{id}")
    public ResponseEntity<List<ReportDTO>> getReports(@PathVariable int id) {
        return ResponseEntity.ok(reportService.getSentReports(id));
    }

    @GetMapping(path = "/reports/received/{id}")
    public ResponseEntity<List<ReportDTO>> getRecievedReports(@PathVariable int id) {
        return ResponseEntity.ok(reportService.getReceivedReports(id));
    }

    @DeleteMapping(path = "/reports/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable int id) {
        return ResponseEntity.ok(reportService.deleteReport(id));
    }

    @DeleteMapping(path = "/messages/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        return ResponseEntity.ok(messageService.deleteMessage(id));
    }

    @GetMapping(path = "/posts/{id}")
    public ResponseEntity<List<PostDTO>> getPosts(Principal principal, @PathVariable int id){
        return ResponseEntity.ok(postService.getPosts(adminService.getUser(principal.getName()).getId(), id));
    }

    @DeleteMapping(path = "/posts/{id}") ResponseEntity<PostDTO> deletePost(@PathVariable int id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }
}
