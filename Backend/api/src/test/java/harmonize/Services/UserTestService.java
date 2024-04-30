package harmonize.Services;

import java.util.List;

import harmonize.DTOs.UserDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.ReportDTO;
import harmonize.DTOs.RoleDTO;

public class UserTestService extends AbstractUserTestService {

    ObjectMapper mapper = new ObjectMapper();

    public UserTestService(String username, String password) {
        super(username, password);
    }

    @Override
    public void setConnection(String hostname, int port) {
        super.setConnection(hostname, port);
        setUser(getSelf().getBody());
    }

    public ResponseEntity<UserDTO> getSelf() {
        return requestService.requestUser(auth, url + port + "/users", HttpMethod.GET);
    }

    public ResponseEntity<UserDTO> updateSelf(UserDTO user) {
        return requestService.requestUser(auth, url + port + "/users", HttpMethod.PUT, user);
    }

    public ResponseEntity<JsonNode> deleteSelf() {
        return requestService.requestJson(auth, url + port + "/users", HttpMethod.DELETE);
    }

    public ResponseEntity<UserDTO> getUserById(int id) {
        return requestService.requestUser(auth, url + port + "/users/id/" + id, HttpMethod.GET);
    }

    public ResponseEntity<List<RoleDTO>> getRoles() {
        return requestService.requestRolesList(auth, url + port + "/users/roles", HttpMethod.GET);
    }

    public ResponseEntity<List<UserDTO>> getFriends() {
        return requestService.requestUserList(auth, url + port + "/users/friends", HttpMethod.GET);
    }

    public ResponseEntity<List<UserDTO>> getRecommendedFriends() {
        return requestService.requestUserList(auth, url + port + "/users/friends/recommended", HttpMethod.GET);
    }

    public ResponseEntity<List<UserDTO>> getFriendInvites() {
        return requestService.requestUserList(auth, url + port + "/users/friends/invites", HttpMethod.GET);
    }

    public ResponseEntity<JsonNode> addFriend(int id) {
        return requestService.requestJson(auth, url + port + "/users/friends/" + id, HttpMethod.POST);
    }

    public ResponseEntity<JsonNode> removeFriend(int id) {
        return requestService.requestJson(auth, url + port + "/users/friends/" + id, HttpMethod.DELETE);
    }

    public ResponseEntity<List<ReportDTO>> getSentReports() {
        return requestService.requestReports(auth, url + port + "/users/reports", HttpMethod.GET);
    }

    public ResponseEntity<ReportDTO> sendReport(MessageDTO message, String reportText) throws JsonProcessingException {
        ObjectNode body = mapper.createObjectNode();
        body.putPOJO("message", message);
        body.put("reportText", reportText);
        return requestService.requestReport(auth, url + port + "/users/reports", HttpMethod.POST, body);
    }

    public ResponseEntity<JsonNode> deleteReport(ReportDTO report) {
        return requestService.requestJson(auth, url + port + "/users/reports/" + report.getId(), HttpMethod.DELETE);
    }

    public ResponseEntity<UserDTO> getAdminRequest() {
        return requestService.requestUser(auth, url + port + "/admins", HttpMethod.GET);
    }

    public ResponseEntity<UserDTO> getModeratorRequest() {
        return requestService.requestUser(auth, url + port + "/moderators", HttpMethod.GET);
    }
}
