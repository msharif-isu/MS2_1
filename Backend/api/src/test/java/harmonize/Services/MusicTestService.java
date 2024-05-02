package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class MusicTestService {
    private int port;
    private String url;

    @Getter @Setter private AbstractUserTestService user;

    @Autowired
    protected RequestService requestService;

    public MusicTestService(AbstractUserTestService user) {
        this.user = user;
    }

    public void setConnection(String hostname, int port) {
        this.url = "https://" + hostname + ":";
        this.port = port;
    }

    public ResponseEntity<JsonNode> getSearch(Object search) throws JsonProcessingException {
        return requestService.requestJson(user.getAuth(), url + port + "/music", HttpMethod.POST, search);
    }

    public ResponseEntity<JsonNode> getSong(Object song) {
        return requestService.requestJson(user.getAuth(), url + port + "/music/songs/" + song, HttpMethod.GET);
    }
}
