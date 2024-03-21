package harmonize.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.DTOs.ReportDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Services.MessageService;
import harmonize.Services.ModeratorService;
import harmonize.Services.ReportService;
import harmonize.Services.UserService;

@RestController
@RequestMapping("/moderators")
public class ModeratorController {
    private ModeratorService moderatorService;
    private UserService userService;
    private ReportService reportService;
    private MessageService messageService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService, UserService userService, ReportService reportService, MessageService messageService) {
        this.moderatorService = moderatorService;
        this.userService = userService;
        this.reportService = reportService;
        this.messageService = messageService;
    }


    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(moderatorService.getAllUsers());
    }

    @GetMapping(path = "")
    public ResponseEntity<UserDTO> getSelf(Principal principal){
        return ResponseEntity.ok(moderatorService.getUser(principal.getName()));
    }

    @DeleteMapping(path = "")
    public ResponseEntity<String> deleteSelf(Principal principal){
        return ResponseEntity.ok(userService.deleteUser(moderatorService.getUser(principal.getName()).getId()));
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id){
        return ResponseEntity.ok(moderatorService.getUser(id));
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        return ResponseEntity.ok(moderatorService.deleteUser(id));
    }

    @GetMapping(path = "/reports")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping(path = "/reports/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable int id) {
        return ResponseEntity.ok(reportService.getReport(id));
    }

    @DeleteMapping(path = "/reports/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable int id) {
        return ResponseEntity.ok(reportService.deleteReport(id));
    }

    @DeleteMapping(path = "/messages/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        return ResponseEntity.ok(messageService.deleteMessage(id));
    }
}
