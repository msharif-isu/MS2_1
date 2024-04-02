package harmonize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;
import harmonize.DTOs.UserDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

    @LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    private static String hostname = "http://localhost:";
    private static AuthDTO auth;
    private static UserDTO user;
    
    @BeforeEach
    public void createUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegisterDTO body = new RegisterDTO("tod", "wilson", "twilson", "todpw");
        ResponseEntity<AuthDTO> registerResponseEntity = null;
        registerResponseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(body, headers),
            AuthDTO.class);
        if (registerResponseEntity.getStatusCode().equals(HttpStatus.OK))
            auth = registerResponseEntity.getBody();

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        ResponseEntity<UserDTO> getSelfResponseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            UserDTO.class);
        if (getSelfResponseEntity.getStatusCode().equals(HttpStatus.OK))
            user = getSelfResponseEntity.getBody();
    }

    @Test
    public void getSelfOkTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            UserDTO.class);
        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getSelfUnauthorizedTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0d2lsc29uIiwiZXhwIjoxNzExMDY1MzA0fQ.fMMD-4NPWook1MEwS_PDRkwFDMmAyZyu7dtWp5Uww1XMiLrGuDmKaUM2oYw1r5dBErJv8jYyHIxjOKxoWergFQ");
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            UserDTO.class);
        
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void updateSelfOkTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        UserDTO body = new UserDTO(user.getId(), "tod2", "wilson2", "twilson2", "My name is tod2.");
        ResponseEntity<UserDTO> responseEntity = null;
        responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.PUT,
            new HttpEntity<UserDTO>(body, headers),
            UserDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO loginBody = new LoginDTO("twilson2", "todpw");
        ResponseEntity<AuthDTO> loginResponseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<LoginDTO>(loginBody, headers),
            AuthDTO.class);
        assertEquals(HttpStatus.OK, loginResponseEntity.getStatusCode());
        
        headers = new HttpHeaders();
        AuthDTO loginReponseBody = loginResponseEntity.getBody();
        if (loginReponseBody == null) {
            fail();
            return;
        }
        headers.set("Authorization", "Bearer " + loginReponseBody.getAccessToken());
        ResponseEntity<UserDTO> getSelfResponseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            UserDTO.class);

        assertEquals(HttpStatus.OK, getSelfResponseEntity.getStatusCode());

        assertEquals(body.getUsername(), getSelfResponseEntity.getBody().getUsername());
        assertEquals(body.getFirstName(), getSelfResponseEntity.getBody().getFirstName());
        assertEquals(body.getLastName(), getSelfResponseEntity.getBody().getLastName());
        assertEquals(body.getBio(), getSelfResponseEntity.getBody().getBio());
    }

    @Test
    public void updateSelfUsernameTakenTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegisterDTO registerBody = new RegisterDTO("phil", "swift", "pswift", "philpw");
        restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(registerBody, headers),
            AuthDTO.class);
        

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        UserDTO body = new UserDTO(user.getId(), "tod4", "wilson4", "pswift", "My name is tod4.");
        ResponseEntity<UserDTO> responseEntity = null;
        responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.PUT,
            new HttpEntity<UserDTO>(body, headers),
            UserDTO.class);
        
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void updateSelfUsernameEmptyTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        UserDTO body = new UserDTO(user.getId(), "tod3", "wilson3", "", "My name is tod3.");
        ResponseEntity<UserDTO> responseEntity = null;
        responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.PUT,
            new HttpEntity<UserDTO>(body, headers),
            UserDTO.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteSelfOkTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        ResponseEntity<Object> deleteResponseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            Object.class);
        
        assertEquals(HttpStatus.OK, deleteResponseEntity.getStatusCode());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO body = new LoginDTO("twilson", "todpw");
        ResponseEntity<AuthDTO> loginResponseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<LoginDTO>(body, headers),
            AuthDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, loginResponseEntity.getStatusCode());
    }

    @Test
    public void adminRequestUnauthorizedTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(
            hostname + port + "/admin",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            UserDTO.class);
        
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
    
}
