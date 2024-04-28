package harmonize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUtil {
    
    @LocalServerPort
    @Getter private int port;
    @Getter private String hostname = "localhost";

    @Autowired
    protected TestRestTemplate restTemplate;

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

        adminTestService.setAuth(authTestService.login(new LoginDTO(adminTestService.getUsername(), adminTestService.getPassword())).getBody());
        adminTestService.setUser(adminTestService.getSelf().getBody());

        modTestService.setAuth(authTestService.login(new LoginDTO(modTestService.getUsername(), modTestService.getPassword())).getBody());
        modTestService.setUser(modTestService.getSelf().getBody());

        ResponseEntity<AuthDTO> responseEntity = authTestService.register(new RegisterDTO("tod", "wilson", todTestService.getUsername(), todTestService.getPassword()));
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            todTestService.setAuth(responseEntity.getBody());
        else
            todTestService.setAuth(authTestService.login(new LoginDTO(todTestService.getUsername(), todTestService.getPassword())).getBody());
        todTestService.setUser(todTestService.getSelf().getBody());

        responseEntity = authTestService.register(new RegisterDTO("bob", "roberts", bobTestService.getUsername(), bobTestService.getPassword()));
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            bobTestService.setAuth(responseEntity.getBody());
        else
            bobTestService.setAuth(authTestService.login(new LoginDTO(bobTestService.getUsername(), bobTestService.getPassword())).getBody());
        bobTestService.setUser(bobTestService.getSelf().getBody());

        responseEntity = authTestService.register(new RegisterDTO("sam", "jones", samTestService.getUsername(), samTestService.getPassword()));
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            samTestService.setAuth(responseEntity.getBody());
        else
            samTestService.setAuth(authTestService.login(new LoginDTO(samTestService.getUsername(), samTestService.getPassword())).getBody());
        samTestService.setUser(samTestService.getSelf().getBody());

        musicTestService.setAuth(todTestService.getAuth());
        musicTestService.setUser(todTestService.getUser());
    }

    @AfterEach
    public void teardown() {
        ResponseEntity<List<UserDTO>> usersResponseEntity = adminTestService.getUsers();
        if (usersResponseEntity.getStatusCode() != HttpStatus.OK)
            return;
        
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
