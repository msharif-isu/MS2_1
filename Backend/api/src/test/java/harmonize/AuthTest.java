package harmonize;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTest {

    @LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    private static String hostname = "http://localhost:";

    @Test
	public void loginOkTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegisterDTO registerBody = new RegisterDTO("kyle", "davis", "kdavis", "kylepw");
        restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(registerBody, headers),
            AuthDTO.class);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO body = new LoginDTO("kdavis", "kylepw");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<LoginDTO>(body, headers),
            AuthDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

    @Test
	public void loginUnautorizedTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO body = new LoginDTO("tbrown", "NotTimPassword");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<LoginDTO>(body, headers),
            AuthDTO.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

    @Test
    public void registerOkTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegisterDTO body = new RegisterDTO("bill", "miller", "bmiller", "billpw");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(body, headers),
            AuthDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

    @Test
    public void registerUsernameTakenTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegisterDTO body = new RegisterDTO("kim", "jones", "kjones", "kimpw");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(body, headers),
            AuthDTO.class);

        responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(body, headers),
            AuthDTO.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}

    @Test
    public void registerUsernameEmptyTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RegisterDTO body = new RegisterDTO("first", "last", "", "password");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<RegisterDTO>(body, headers),
            AuthDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
}
