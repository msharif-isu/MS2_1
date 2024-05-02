package harmonize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;
import harmonize.Services.AdminTestService;
import harmonize.Services.AuthTestService;
import harmonize.Services.ModeratorTestService;
import harmonize.Services.MusicTestService;
import harmonize.Services.RequestService;
import harmonize.Services.UserTestService;
import harmonize.Services.WebSocketTestService;
import lombok.Getter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestUtil {
    
    @LocalServerPort
    @Getter private int port;
    @Getter private String hostname = "coms-309-032.class.las.iastate.edu";

    @Autowired
    protected RequestService requestService;
    
    @Autowired protected AuthTestService authTestService;
    @Autowired protected AdminTestService adminTestService;
    @Autowired protected ModeratorTestService modTestService;
    @Autowired protected UserTestService todTestService;
    @Autowired protected UserTestService bobTestService;
    @Autowired protected UserTestService samTestService;
    @Autowired protected MusicTestService musicTestService;
    protected WebSocketTestService chatSocket;

    @BeforeEach
    public void setup() {
        authTestService.setConnection(hostname, port);
        adminTestService.setConnection(hostname, port);
        modTestService.setConnection(hostname, port);
        todTestService.setConnection(hostname, port);
        bobTestService.setConnection(hostname, port);
        samTestService.setConnection(hostname, port);
        musicTestService.setConnection(hostname, port);

        musicTestService.setUser(todTestService);
    }

    @AfterEach
    public void teardown() {
        ResponseEntity<List<UserDTO>> usersResponseEntity = adminTestService.getUsers();
        if (usersResponseEntity.getStatusCode() != HttpStatus.OK) {
            fail("Admin user could not get users.");
            return;
        }
        
        List<UserDTO> usersBody = usersResponseEntity.getBody();
        if (usersBody == null) {
            fail();
            return;
        }
        for (UserDTO user : usersBody) {
            ResponseEntity<List<RoleDTO>> rolesResponseEntity = adminTestService.getUserRoles(user.getId());
            if (rolesResponseEntity.getStatusCode() != HttpStatus.OK)
                continue;

            List<RoleDTO> roleBody = rolesResponseEntity.getBody();
            if (roleBody == null) {
                fail();
                return;
            }
            
            if (!roleBody.stream().anyMatch(role -> (role.getName().equals("ADMIN") || role.getName().equals("MODERATOR")))) {
                HttpStatusCode statusCode = adminTestService.deleteUser(user.getId()).getStatusCode();
                assertEquals(HttpStatus.OK, statusCode, user.toString());
            }
        }
    }
}
