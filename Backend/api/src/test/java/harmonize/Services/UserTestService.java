package harmonize.Services;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.UserDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;

import harmonize.DTOs.RoleDTO;

public class UserTestService {
    private int port;
    private String url;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private UserDTO user;
    @Getter @Setter private AuthDTO auth;

    @Getter WebSocketTestClient chatSocket;

    @Autowired
    private RequestService requestService;

    public UserTestService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setConnection(String url, int port) {
        this.url = url;
        this.port = port;
        chatSocket = new WebSocketTestClient(URI.create("ws://localhost:" + port + "/chats?username=" + username + "&password=" + password));
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
