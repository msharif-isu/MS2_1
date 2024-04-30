package harmonize.Services;

import harmonize.DTOs.UserDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public class ModeratorTestService extends AbstractUserTestService {

    public ModeratorTestService(String username, String password) {
        super(username, password);
    }

    @Override
    public void setConnection(String hostname, int port) {
        super.setConnection(hostname, port);
        setUser(getSelf().getBody());
    }

    public ResponseEntity<UserDTO> getSelf() {
        return requestService.requestUser(auth, url + port + "/moderators", HttpMethod.GET);
    }
    
    public ResponseEntity<JsonNode> deleteSelf() {
        return requestService.requestJson(auth, url + port + "/moderators", HttpMethod.DELETE);
    }
}
