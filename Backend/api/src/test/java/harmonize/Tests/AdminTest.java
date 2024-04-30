package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import harmonize.TestUtil;
import harmonize.DTOs.UserDTO;

public class AdminTest extends TestUtil {

    @Test
    public void getSelfOkTest() throws Exception {
        ResponseEntity<UserDTO> responseEntity = adminTestService.getSelf();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(adminTestService.getUser(), responseEntity.getBody());
    }

    @Test
    public void getUsersOkTest() throws Exception {
        ResponseEntity<List<UserDTO>> responseEntity = adminTestService.getUsers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserDTO> body = responseEntity.getBody();
        if (body == null) {
            fail();
            return;
        }
        assertTrue(body.contains(todTestService.getUser()), "Tod was not found in user list.");
    }

    @Test
    public void getUserByIdOkTest() throws Exception {
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
