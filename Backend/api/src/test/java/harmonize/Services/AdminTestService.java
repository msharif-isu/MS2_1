package harmonize.Services;

import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


public class AdminTestService extends AbstractUserTestService {

    public AdminTestService(String username, String password) {
        super(username, password);
    }

    @Override
    public void setConnection(String hostname, int port) {
        super.setConnection(hostname, port);
        setUser(getSelf().getBody());
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
