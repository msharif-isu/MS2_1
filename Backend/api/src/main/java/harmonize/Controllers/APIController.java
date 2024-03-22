package harmonize.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import harmonize.Services.APIService;

@RestController
@RequestMapping("/api")
public class APIController {
    private APIService apiService;

    @Autowired
    public APIController(APIService apiService) {
        this.apiService = apiService;
    }
    
    @GetMapping(path = "/token")
    public ResponseEntity<JsonNode> getAPIToken() throws JsonMappingException, JsonProcessingException {
        return ResponseEntity.ok(apiService.getAPIToken());
    }

    @GetMapping(path = "/search/{search}")
    public ResponseEntity<JsonNode> search(@PathVariable String search) throws JsonMappingException, JsonProcessingException {
        return ResponseEntity.ok(apiService.search(search));
    }
}
