package harmonize;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

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
	public void loginTest() throws Exception {
		String url = hostname + this.port + "/auth/login";
        AuthDTO auth = this.restTemplate.postForObject(url, new LoginDTO("tbrown", "timpw"), AuthDTO.class);
        assertNotNull(auth);
	}

    @Test
    public void registerTest() throws Exception {
		String url = hostname + this.port + "/auth/register";
        AuthDTO auth = this.restTemplate.postForObject(url, new RegisterDTO("bill", "miller", "bmiller", "billpw"), AuthDTO.class);
        assertNotNull(auth);
	}
}
