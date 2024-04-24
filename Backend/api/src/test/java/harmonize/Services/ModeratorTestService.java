package harmonize.Services;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;

public class ModeratorTestService {
    private int port;
    private String url;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private UserDTO user;
    @Getter @Setter private AuthDTO auth;

    @Autowired
    private RequestService requestService;

    public ModeratorTestService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setConnection(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public ResponseEntity<UserDTO> getSelf() {
        return requestService.requestUser(auth, url + port + "/moderators", HttpMethod.GET);
    }
    
    public ResponseEntity<String> deleteSelf() {
        return requestService.requestString(auth, url + port + "/moderators", HttpMethod.DELETE);
    }
}
