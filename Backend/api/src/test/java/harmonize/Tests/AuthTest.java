package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import harmonize.TestUtil;
import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;

public class AuthTest extends TestUtil {

    @Test
    public void loginOkTest() throws Exception {
        authTestService.register(new RegisterDTO("tod", "wilson", todTestService.getUsername(), todTestService.getPassword()));
        ResponseEntity<AuthDTO> responseEntity = authTestService.login(new LoginDTO(todTestService.getUsername(), todTestService.getPassword()));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void loginUnautorizedTest() throws Exception {
        authTestService.register(new RegisterDTO("tod", "wilson", todTestService.getUsername(), todTestService.getPassword()));
        ResponseEntity<AuthDTO> responseEntity = authTestService.login(new LoginDTO(todTestService.getUsername(), "INVALID_PW"));

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void registerOkTest() throws Exception {
        todTestService.deleteSelf();
        
        ResponseEntity<AuthDTO> responseEntity = authTestService.register(new RegisterDTO("tod", "wilson", todTestService.getUsername(), todTestService.getPassword()));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void registerUsernameTakenTest() throws Exception {
        authTestService.register(new RegisterDTO("tod", "wilson", todTestService.getUsername(), todTestService.getPassword()));
        ResponseEntity<AuthDTO> responseEntity = authTestService.register(new RegisterDTO("tim", "wilson", todTestService.getUsername(), "timpw"));

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void registerUsernameEmptyTest() throws Exception {
        ResponseEntity<AuthDTO> responseEntity = authTestService.register(new RegisterDTO("tim", "wilson", "", "timpw"));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
