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

import com.fasterxml.jackson.databind.ObjectMapper;

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
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
	public void loginTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object body = new LoginDTO("tbrown", "timpw");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(objectMapper.writeValueAsString(body), headers),
            AuthDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

    @Test
    public void registerTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object body = new RegisterDTO("bill", "miller", "bmiller", "billpw");
        ResponseEntity<AuthDTO> responseEntity = restTemplate.exchange(
            hostname + this.port + "/auth/register",
            HttpMethod.POST,
            new HttpEntity<>(objectMapper.writeValueAsString(body), headers),
            AuthDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
}
