package harmonize.Services;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class APIService {
    private JsonNode apiAuthentication;

    private long apiExpiration;

    private String apiURL;

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public APIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        this.apiURL = "https://api.spotify.com/v1";
        this.apiExpiration = 0;
    }

    public JsonNode getAPIToken() throws JsonMappingException, JsonProcessingException {
        final String url = "https://accounts.spotify.com/api/token";
        final String clientID = "d3eacb5996b54e6fbb79ae2c4902a417";
        final String clientSecret = "4838a780565949b6b02e6f4f97f4ba0b";

        if (apiExpiration > System.currentTimeMillis())
            return apiAuthentication;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String encodedAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", clientID, clientSecret).getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        this.apiExpiration = System.currentTimeMillis() + responseJson.get("expires_in").asInt() * 1000;

        return (apiAuthentication = responseJson);
    }

    public JsonNode search(String search) throws JsonMappingException, JsonProcessingException {
        String urlEnd = "/search";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken().get("access_token").toString().replaceAll("\"", ""));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(apiURL + urlEnd)
                        .queryParam("q", search)
                        .queryParam("type", "album,track,artist")
                        .queryParam("market", "US")
                        .queryParam("limit", "3")
                        .encode()
                        .toUriString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JsonNode responseJson = objectMapper.readTree(response.getBody());

        return responseJson;
    }
}
