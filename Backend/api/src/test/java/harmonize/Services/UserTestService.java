package harmonize.Services;

import java.util.List;

import harmonize.DTOs.UserDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import harmonize.DTOs.RoleDTO;

public class UserTestService extends AbstractUserTestService {

    public UserTestService(String username, String password) {
        super(username, password);
    }

    public ResponseEntity<UserDTO> getSelf() {
        return requestService.requestUser(auth, url + port + "/users", HttpMethod.GET);
    }

    public ResponseEntity<UserDTO> updateSelf(UserDTO user) {
        return requestService.requestUser(auth, url + port + "/users", HttpMethod.PUT, user);
    }

    public ResponseEntity<String> deleteSelf() {
        return requestService.requestString(auth, url + port + "/users", HttpMethod.DELETE);
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

    public ResponseEntity<String> addFriend(int id) {
        return requestService.requestString(auth, url + port + "/users/friends/" + id, HttpMethod.POST);
    }

    public ResponseEntity<String> removeFriend(int id) {
        return requestService.requestString(auth, url + port + "/users/friends/" + id, HttpMethod.DELETE);
    }

    public ResponseEntity<UserDTO> getAdminRequest() {
        return requestService.requestUser(auth, url + port + "/admins", HttpMethod.GET);
    }

    public ResponseEntity<UserDTO> getModeratorRequest() {
        return requestService.requestUser(auth, url + port + "/moderators", HttpMethod.GET);
    }
}
