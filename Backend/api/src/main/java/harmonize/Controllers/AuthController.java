package harmonize.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.Services.AuthService;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO user) {    
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthDTO> register(@RequestBody RegisterDTO user) {
        return ResponseEntity.ok(authService.register(user));
    }
}
