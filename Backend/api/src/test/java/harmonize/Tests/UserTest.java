package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import harmonize.TestUtil;
import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.MessageDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.DTOs.ReportDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;

public class UserTest extends TestUtil {

    @Test
    public void getSelfOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = todTestService.getSelf();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(todTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void getSelfUnauthorizedTest() throws Exception {
        String invalidToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0d2lsc29uIiwiZXhwIjoxNzExMDY1MzA0fQ.fMMD-4NPWook1MEwS_PDRkwFDMmAyZyu7dtWp5Uww1XMiLrGuDmKaUM2oYw1r5dBErJv8jYyHIxjOKxoWergFQ";
        String validToken = todTestService.getAuth().getAccessToken();
        todTestService.getAuth().setAccessToken(invalidToken);
        assertEquals(HttpStatus.UNAUTHORIZED, todTestService.getSelf().getStatusCode());
        todTestService.getAuth().setAccessToken(validToken);
    }

    @Test
    public void userGetUserByIdOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = todTestService.getUserById(bobTestService.getUser().getId());
        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bobTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void userGetUserByIdNotFoundOkTest() throws Exception {
        assertEquals(HttpStatus.NOT_FOUND, todTestService.getUserById(0).getStatusCode());
    }

    @Test
    public void getAdminByIdNotFoundTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = todTestService.getUserById(adminTestService.getUser().getId());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getModeratorByIdNotFoundTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = todTestService.getUserById(modTestService.getUser().getId());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateSelfOkTest() throws Exception {
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
    public void updateSelfUsernameTakenTest() throws Exception {
        authTestService.register(new RegisterDTO("phil", "swift", "pswift", "philpw"));
        
        UserDTO newInfo = new UserDTO(todTestService.getUser().getId(), "tod4", "wilson4", "pswift", "My name is tod4.");
        ResponseEntity<UserDTO> responseEntity = todTestService.updateSelf(newInfo);
        
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void updateSelfUsernameEmptyTest() throws Exception {
        UserDTO newInfo = new UserDTO(todTestService.getUser().getId(), "tod2", "wilson2", "", "My name is tod2.");
        ResponseEntity<UserDTO> responseEntity = todTestService.updateSelf(newInfo);
        
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteSelfOkTest() throws Exception {
        assertEquals(HttpStatus.OK, todTestService.deleteSelf().getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, todTestService.getSelf().getStatusCode());
    }

    @Test
    public void getRolesOkTest() throws Exception {
        ResponseEntity<List<RoleDTO>> responseEntity = todTestService.getRoles();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<RoleDTO> body = responseEntity.getBody();
        if (body == null) {
            fail();
            return;
        }
        assertTrue(body.stream().anyMatch(role -> role.getName().equals("USER")), "USER role was not found in response.");
    }

    @Test
    public void getFriendsOkTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());

        ResponseEntity<List<UserDTO>> responseEntity = todTestService.getFriends();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserDTO> body = responseEntity.getBody();
        if (body == null) {
            fail();
            return;
        }
        assertTrue(body.contains(bobTestService.getUser()), "Bob was not found in friends list.");
    }

    @Test
    public void getRecommendedFriendsOkTest() throws Exception {
        ResponseEntity<List<UserDTO>> responseEntity = todTestService.getRecommendedFriends();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getFriendInvitesOkTest() throws Exception {
        bobTestService.addFriend(todTestService.getUser().getId());
        ResponseEntity<List<UserDTO>> responseEntity = todTestService.getFriendInvites();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserDTO> body = responseEntity.getBody();
        if (body == null) {
            fail();
            return;
        }
        assertTrue(body.contains(bobTestService.getUser()), "Bob was not found in friend invites list.");
    }

    @Test
    public void addFriendInviteOkTest() throws Exception {
        assertEquals(HttpStatus.OK, todTestService.addFriend(bobTestService.getUser().getId()).getStatusCode());
    }

    @Test
    public void addFriendNotFoundTest() throws Exception {
        assertEquals(HttpStatus.NOT_FOUND, todTestService.addFriend(0).getStatusCode());
    }

    @Test
    public void addFriendInviteAlreadySentTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        assertEquals(HttpStatus.CONFLICT, todTestService.addFriend(bobTestService.getUser().getId()).getStatusCode());
    }

    @Test
    public void addFriendOkTest() throws Exception {
        bobTestService.addFriend(todTestService.getUser().getId());
        assertEquals(HttpStatus.OK, todTestService.addFriend(bobTestService.getUser().getId()).getStatusCode());
    }

    @Test
    public void addFriendAlreadyFriendTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        assertEquals(HttpStatus.CONFLICT, todTestService.addFriend(bobTestService.getUser().getId()).getStatusCode());
    }

    @Test
    public void addFriendSelfTest() throws Exception {
        assertEquals(HttpStatus.BAD_REQUEST, todTestService.addFriend(todTestService.getUser().getId()).getStatusCode());
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void sendReportOkTest() throws Exception {
        bobTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        todTestService.createConversation(List.of(todTestService.getUser().getId(), bobTestService.getUser().getId()));
        Thread.sleep(5000);

        ConversationDTO conversation = bobTestService.getChatSocket().getConversations().stream()
            .filter(item -> (item.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
            .findAny()
            .get();
        
        String text = "Mean Message";
        bobTestService.getChatSocket().send(new MessageDTO(conversation, text));
        Thread.sleep(1000);

        MessageDTO message = bobTestService.getChatSocket().getChats().stream()
            .filter(item -> (item.getText().equals(text)))
            .findAny()
            .get();
        Thread.sleep(1000);

        String reportText = "It was offensive";
        ResponseEntity<ReportDTO> responseEntity = todTestService.sendReport(message, reportText);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void sendReportMessageNotFoundTest() throws Exception {
        bobTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        todTestService.createConversation(List.of(todTestService.getUser().getId(), bobTestService.getUser().getId()));
        Thread.sleep(5000);

        ConversationDTO conversation = bobTestService.getChatSocket().getConversations().stream()
            .filter(item -> (item.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
            .findAny()
            .get();
        Thread.sleep(1000);

        MessageDTO message = new MessageDTO(conversation, "Hello");
        message.setId(0);

        String reportText = "It was offensive";
        try {
            ResponseEntity<ReportDTO> responseEntity = todTestService.sendReport(message, reportText);
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (RestClientException e) {}
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void sendReportMessageNotFound2Test() throws Exception {
        bobTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        todTestService.createConversation(List.of(todTestService.getUser().getId(), bobTestService.getUser().getId()));
        Thread.sleep(5000);

        ConversationDTO conversation = bobTestService.getChatSocket().getConversations().stream()
            .filter(item -> (item.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
            .findAny()
            .get();
        
        String text = "Mean Message";
        bobTestService.getChatSocket().send(new MessageDTO(conversation, text));
        Thread.sleep(1000);

        MessageDTO message = bobTestService.getChatSocket().getChats().stream()
            .filter(item -> (item.getText().equals(text)))
            .findAny()
            .get();
        Thread.sleep(1000);

        String reportText = "It was offensive";
        try {
            ResponseEntity<ReportDTO> responseEntity = samTestService.sendReport(message, reportText);
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        } catch (RestClientException e) {}
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void deleteReportOkTest() throws Exception {
        bobTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        todTestService.createConversation(List.of(todTestService.getUser().getId(), bobTestService.getUser().getId()));
        Thread.sleep(5000);

        ConversationDTO conversation = bobTestService.getChatSocket().getConversations().stream()
            .filter(item -> (item.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
            .findAny()
            .get();
        
        String text = "Mean Message";
        bobTestService.getChatSocket().send(new MessageDTO(conversation, text));
        Thread.sleep(1000);

        MessageDTO message = bobTestService.getChatSocket().getChats().stream()
            .filter(item -> (item.getText().equals(text)))
            .findAny()
            .get();
        Thread.sleep(1000);

        String reportText = "It was offensive";
        ReportDTO report = todTestService.sendReport(message, reportText).getBody();
        assertEquals(HttpStatus.OK, todTestService.deleteReport(report).getStatusCode());
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "DOCKER_RUNNING", matches = "true")
    public void getReportsOkTest() throws Exception {
        bobTestService.getChatSocket().connect();

        todTestService.addFriend(bobTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        todTestService.createConversation(List.of(todTestService.getUser().getId(), bobTestService.getUser().getId()));
        Thread.sleep(5000);

        ConversationDTO conversation = bobTestService.getChatSocket().getConversations().stream()
            .filter(item -> (item.getMembers().containsAll(Set.of(todTestService.getUser(), bobTestService.getUser()))))
            .findAny()
            .get();
        
        String text = "Mean Message";
        bobTestService.getChatSocket().send(new MessageDTO(conversation, text));
        Thread.sleep(1000);

        MessageDTO message = bobTestService.getChatSocket().getChats().stream()
            .filter(item -> (item.getText().equals(text)))
            .findAny()
            .get();
        Thread.sleep(1000);

        String reportText = "It was offensive";
        todTestService.sendReport(message, "It was offensive");
        ResponseEntity<List<ReportDTO>> responseEntity = todTestService.getSentReports();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ReportDTO> body = responseEntity.getBody();
        if (body == null) {
            fail();
            return;
        }
        assertTrue(body.stream()
            .anyMatch(item -> (item.getMessage().equals(message) && item.getReportText().equals(reportText)))
        );
    }

    @Test
    public void setIconOkTest() throws Exception {
        File icon = new File("./target/test-classes/test-icon1.jpeg");
        if (!icon.exists())
            fail("Test icon not found.");
        ResponseEntity<byte[]> responseEntity = todTestService.postIcon(icon);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void userCreateConversationOKTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        todTestService.addFriend(samTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        samTestService.addFriend(todTestService.getUser().getId());

        ResponseEntity<ConversationDTO> responseEntity = todTestService.createConversation(List.of(
            todTestService.getUser().getId(), 
            bobTestService.getUser().getId(), 
            samTestService.getUser().getId()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getIconOkTest() throws Exception {
        File icon = new File("./target/test-classes/test-icon1.jpeg");
        if (!icon.exists())
            fail("Test icon not found.");
        assertEquals(HttpStatus.OK, todTestService.postIcon(icon).getStatusCode());
        ResponseEntity<byte[]> responseEntity = todTestService.getIcon();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(Files.readAllBytes(icon.toPath()), responseEntity.getBody());
    }

    @Test
    public void getIconNotFoundTest() throws Exception {
        ResponseEntity<byte[]> responseEntity = todTestService.getIcon();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void userCreateConversationNotFriendTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        todTestService.addFriend(samTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());

        ResponseEntity<ConversationDTO> responseEntity = todTestService.createConversation(List.of(
            todTestService.getUser().getId(), 
            bobTestService.getUser().getId(), 
            samTestService.getUser().getId()));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getOtherIconOkTest() throws Exception {
        File icon = new File("./target/test-classes/test-icon1.jpeg");
        if (!icon.exists())
            fail("Test icon not found.");
        assertEquals(HttpStatus.OK, todTestService.postIcon(icon).getStatusCode());
        ResponseEntity<byte[]> responseEntity = bobTestService.getIcon(todTestService.getUser().getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(Files.readAllBytes(icon.toPath()), responseEntity.getBody());
    }

    @Test
    public void setOverwriteIconOkTest() throws Exception {
        File icon1 = new File("./target/test-classes/test-icon1.jpeg");
        File icon2 = new File("./target/test-classes/test-icon2.jpeg");
        if (!icon1.exists() || !icon2.exists())
            fail("Test icons not found.");
        assertEquals(HttpStatus.OK, todTestService.postIcon(icon1).getStatusCode());
        assertEquals(HttpStatus.OK, todTestService.postIcon(icon2).getStatusCode());
        ResponseEntity<byte[]> responseEntity = todTestService.getIcon();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(Files.readAllBytes(icon2.toPath()), responseEntity.getBody());
    }

    @Test
    public void deleteIconOkTest() throws Exception {
        File icon = new File("./target/test-classes/test-icon1.jpeg");
        if (!icon.exists())
            fail("Test icon not found.");
        assertEquals(HttpStatus.OK, todTestService.postIcon(icon).getStatusCode());
        assertEquals(HttpStatus.OK, todTestService.deleteIcon().getStatusCode());
        ResponseEntity<byte[]> responseEntity = todTestService.getIcon();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void userLeaveConversationOkTest() throws Exception {
        todTestService.addFriend(bobTestService.getUser().getId());
        todTestService.addFriend(samTestService.getUser().getId());
        bobTestService.addFriend(todTestService.getUser().getId());
        samTestService.addFriend(todTestService.getUser().getId());

        ConversationDTO conversation = todTestService.createConversation(List.of(
            todTestService.getUser().getId(), 
            bobTestService.getUser().getId(), 
            samTestService.getUser().getId()))
            .getBody();

        if (conversation == null) {
            fail();
            return;
        }
        
        ResponseEntity<String> responseEntity = todTestService.leaveConversation(conversation);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void adminRequestUnauthorizedTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = todTestService.getAdminRequest();
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void moderatorRequestUnauthorizedTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = todTestService.getModeratorRequest();
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
