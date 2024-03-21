package harmonize;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.AuthDTO;
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
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    public void createUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object body = new RegisterDTO("tod", "wilson", "twilson", "todpw");
        ResponseEntity<AuthDTO> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(
                hostname + this.port + "/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(objectMapper.writeValueAsString(body), headers),
                AuthDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (responseEntity.getStatusCode().equals(HttpStatus.OK))
            auth = responseEntity.getBody();
    }

    @Test
    public void getSelf() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        HttpEntity<UserDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.GET,
            entity,
            UserDTO.class);
        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getSelfUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0d2lsc29uIiwiZXhwIjoxNzExMDY1MzA0fQ.fMMD-4NPWook1MEwS_PDRkwFDMmAyZyu7dtWp5Uww1XMiLrGuDmKaUM2oYw1r5dBErJv8jYyHIxjOKxoWergFQ");
        HttpEntity<UserDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(
            hostname + port + "/users",
            HttpMethod.GET,
            entity,
            UserDTO.class);
        
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void adminRequestUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        HttpEntity<UserDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(
            hostname + port + "/admin",
            HttpMethod.GET,
            entity,
            UserDTO.class);
        
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
    
}
