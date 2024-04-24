package harmonize.Services;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;

public class AdminTestService {
    private int port;
    private String url;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private UserDTO user;
    @Getter @Setter private AuthDTO auth;

    @Autowired
    private RequestService requestService;

    public AdminTestService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setConnection(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public ResponseEntity<UserDTO> getSelf() {
        return requestService.requestUser(auth, url + port + "/admins", HttpMethod.GET);
    }

    public ResponseEntity<List<UserDTO>> getUsers() {
        return requestService.requestUserList(auth, url + port + "/admins/users", HttpMethod.GET);
    }

    public ResponseEntity<UserDTO> getUser(int id) {
        return requestService.requestUser(auth, url + port + "/admins/users/" + id, HttpMethod.GET);
    }

    public ResponseEntity<String> deleteUser(int id) {
        return requestService.requestString(auth, url + port + "/admins/users/" + id, HttpMethod.DELETE);
    }

    public ResponseEntity<List<RoleDTO>> getUserRoles(int id) {
        return requestService.requestRolesList(auth, url + port + "/admins/roles/" + id, HttpMethod.GET);
    }
}
