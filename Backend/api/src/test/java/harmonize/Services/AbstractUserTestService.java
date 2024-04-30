package harmonize.Services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.DTOs.UserDTO;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractUserTestService {
    protected int port;
    protected String url;
    @Getter @Setter protected String username;
    @Getter @Setter protected String password;
    @Getter @Setter protected UserDTO user;
    @Getter @Setter protected AuthDTO auth;

    @Getter ChatSocketTestService chatSocket;

    @Autowired
    protected RequestService requestService;

    @Autowired
    protected AuthTestService authTestService;

    public AbstractUserTestService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setConnection(String hostname, int port) {
        this.url = "https://" + hostname + ":";
        this.port = port;
        try {
            chatSocket = new ChatSocketTestService(URI.create("wss://" + hostname + ":" + port + "/chats?username=" + username + "&password=" + password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<AuthDTO> responseEntity = authTestService.register(new RegisterDTO("first", "last", username, password));
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            auth = responseEntity.getBody();
        else
            auth = authTestService.login(new LoginDTO(username, password)).getBody();
    }

    public abstract ResponseEntity<UserDTO> getSelf();
}
