package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import harmonize.TestUtil;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;

public class UserTest extends TestUtil {

    @Test
    public void getSelfOkTest() {
        ResponseEntity<UserDTO> responseEntity = todTestService.getSelf();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(todTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void getSelfUnauthorizedTest() {
        String invalidToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0d2lsc29uIiwiZXhwIjoxNzExMDY1MzA0fQ.fMMD-4NPWook1MEwS_PDRkwFDMmAyZyu7dtWp5Uww1XMiLrGuDmKaUM2oYw1r5dBErJv8jYyHIxjOKxoWergFQ";
        String validToken = todTestService.getAuth().getAccessToken();
        todTestService.getAuth().setAccessToken(invalidToken);
        assertEquals(HttpStatus.UNAUTHORIZED, todTestService.getSelf().getStatusCode());
        todTestService.getAuth().setAccessToken(validToken);
    }

    @Test
    public void userGetUserByIdOkTest() {
        ResponseEntity<UserDTO> responseEntity = todTestService.getUserById(bobTestService.getUser().getId());
        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bobTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void userGetUserByIdNotFoundOkTest() {
        assertEquals(HttpStatus.NOT_FOUND, todTestService.getUserById(0).getStatusCode());
    }

    @Test
    public void getAdminByIdNotFoundTest() {
        ResponseEntity<UserDTO> responseEntity = todTestService.getUserById(adminTestService.getUser().getId());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getModeratorByIdNotFoundTest() {
        ResponseEntity<UserDTO> responseEntity = todTestService.getUserById(modTestService.getUser().getId());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateSelfOkTest() {
        UserDTO newInfo = new UserDTO(todTestService.getUser().getId(), "tod2", "wilson2", "twilson2", "My name is tod2.");
        ResponseEntity<UserDTO> responseEntity = todTestService.updateSelf(newInfo);
        
        todTestService.setAuth(authTestService.login(new LoginDTO("twilson2", todTestService.getPassword())).getBody());
        todTestService.setUser(todTestService.getSelf().getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(newInfo.getUsername(), todTestService.getUser().getUsername());
        assertEquals(newInfo.getFirstName(), todTestService.getUser().getFirstName());
        assertEquals(newInfo.getLastName(), todTestService.getUser().getLastName());
        assertEquals(newInfo.getBio(), todTestService.getUser().getBio());
    }

    @Test
    public void updateSelfUsernameTakenTest() {
        authTestService.register(new RegisterDTO("phil", "swift", "pswift", "philpw"));
        
        UserDTO newInfo = new UserDTO(todTestService.getUser().getId(), "tod4", "wilson4", "pswift", "My name is tod4.");
        ResponseEntity<UserDTO> responseEntity = todTestService.updateSelf(newInfo);
        
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void updateSelfUsernameEmptyTest() {
        UserDTO newInfo = new UserDTO(todTestService.getUser().getId(), "tod2", "wilson2", "", "My name is tod2.");
        ResponseEntity<UserDTO> responseEntity = todTestService.updateSelf(newInfo);
        
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteSelfOkTest() {
        assertEquals(HttpStatus.OK, todTestService.deleteSelf().getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, todTestService.getSelf().getStatusCode());
    }

    @Test
    public void getRolesOkTest() {
        ResponseEntity<List<RoleDTO>> responseEntity = todTestService.getRoles();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().stream().anyMatch(role -> role.getName().equals("USER")), "USER role was not found in response.");
    }

    @Test
    public void getFriendsOkTest() {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());

        ResponseEntity<List<UserDTO>> responseEntity = todTestService.getFriends();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains(bobTestService.getUser()), "Bob was not found in friends list.");
    }

    @Test
    public void getRecommendedFriendsOkTest() {
        ResponseEntity<List<UserDTO>> responseEntity = todTestService.getRecommendedFriends();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getFriendInvitesOkTest() {
        bobTestService.addFriend(todTestService.getUser().getId());
        ResponseEntity<List<UserDTO>> responseEntity = todTestService.getFriendInvites();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains(bobTestService.getUser()), "Bob was not found in friend invites list.");
    }

    @Test
    public void userAddFriendInviteOkTest() {
        ResponseEntity<String> responseEntity = todTestService.addFriend(bobTestService.getUser().getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void userAddFriendNotFoundTest() {
        ResponseEntity<String> responseEntity = todTestService.addFriend(0);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void userAddFriendInviteAlreadySentTest() {
        todTestService.addFriend(bobTestService.getUser().getId());

        ResponseEntity<String> responseEntity = todTestService.addFriend(bobTestService.getUser().getId());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void userAddFriendOkTest() {
        bobTestService.addFriend(todTestService.getUser().getId());

        ResponseEntity<String> responseEntity = todTestService.addFriend(bobTestService.getUser().getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void userAddFriendAlreadyFriendTest() {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        
        ResponseEntity<String> responseEntity = todTestService.addFriend(bobTestService.getUser().getId());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void userAddFriendSelfTest() {
        ResponseEntity<String> responseEntity = todTestService.addFriend(todTestService.getUser().getId());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void adminRequestUnauthorizedTest() {
        ResponseEntity<UserDTO> responseEntity = todTestService.getAdminRequest();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void moderatorRequestUnauthorizedTest() {
        ResponseEntity<UserDTO> responseEntity = todTestService.getModeratorRequest();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
