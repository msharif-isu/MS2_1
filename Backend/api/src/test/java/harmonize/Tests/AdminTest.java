package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import harmonize.TestUtil;
import harmonize.DTOs.UserDTO;

public class AdminTest extends TestUtil {

    @Test
    public void adminGetSelfOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = adminTestService.getSelf();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(adminTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void adminGetUsersOkTest() throws Exception {
        ResponseEntity<List<UserDTO>> responseEntity = adminTestService.getUsers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains(todTestService.getUser()), "Tod was not found in user list.");
    }

    @Test
    public void adminGetUserByIdOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = adminTestService.getUser(todTestService.getUser().getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(todTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void getModeratorByIdOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = adminTestService.getUser(modTestService.getUser().getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(modTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void getAdminByIdOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = adminTestService.getUser(adminTestService.getUser().getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(adminTestService.getUser(), responseEntity.getBody());
    }
}
