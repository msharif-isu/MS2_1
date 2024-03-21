package harmonize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WelcomeTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static String hostname = "http://localhost:";

	@Test
	public void welcomeTest() throws Exception {
		ResponseEntity<String> responseEntity = restTemplate.exchange(
            hostname + this.port + "/",
            HttpMethod.GET,
            new HttpEntity<>(null),
            String.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

}