package harmonize.Services;

import harmonize.DTOs.UserDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class ModeratorTestService extends AbstractUserTestService {

    public ModeratorTestService(String username, String password) {
        super(username, password);
    }

    public ResponseEntity<UserDTO> getSelf() {
        return requestService.requestUser(auth, url + port + "/moderators", HttpMethod.GET);
    }
    
    public ResponseEntity<String> deleteSelf() {
        return requestService.requestString(auth, url + port + "/moderators", HttpMethod.DELETE);
    }
}
