package harmonize.Services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.SearchDTO;

public class MusicTestService extends AbstractUserTestService {
    ObjectMapper objectMapper = new ObjectMapper();

    public MusicTestService(String username, String password) {
        super(username, password);
    }

    public ResponseEntity<JsonNode> getSearch(SearchDTO search) throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(search));
        System.out.println(url + port + "/music");
        System.out.println("Bearer "  + auth.getAccessToken());
        return requestService.requestJson(auth, url + port + "/music", HttpMethod.POST, search);
    }
}
