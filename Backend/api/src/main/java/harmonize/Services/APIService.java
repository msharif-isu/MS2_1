package harmonize.Services;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import harmonize.DTOs.SearchDTO;
import harmonize.Entities.Artist;
import harmonize.Entities.Song;
import harmonize.ErrorHandling.Exceptions.InvalidSearchException;
import harmonize.Repositories.ArtistRepository;
import harmonize.Repositories.SongRepository;

@Service
public class APIService {
    private String apiAuthentication;

    private long apiExpiration;

    private String apiURL;

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    private SongRepository songRepository;

    private ArtistRepository artistRepository;

    @Autowired
    public APIService(RestTemplate restTemplate, SongRepository songRepository, ArtistRepository artistRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        this.apiURL = "https://api.spotify.com/v1";
        this.apiExpiration = 0;
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }

    public String getAPIToken() {
        if (apiExpiration > System.currentTimeMillis())
            return apiAuthentication;

        final String url = "https://accounts.spotify.com/api/token";
        final String clientID = "d3eacb5996b54e6fbb79ae2c4902a417";
        final String clientSecret = "4838a780565949b6b02e6f4f97f4ba0b";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String encodedAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", clientID, clientSecret).getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            responseJson = objectMapper.readTree(response.getBody());
        }
        catch(Exception e) {
            throw new InvalidSearchException("Unable to retrieve token");
        }

        this.apiExpiration = System.currentTimeMillis() + responseJson.get("expires_in").asInt() * 1000;

        return (apiAuthentication = responseJson.get("access_token").asText());
    }

    public JsonNode search(SearchDTO search) {
        String urlEnd = "/search";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(apiURL + urlEnd)
                        .queryParam("q", search.getQ())
                        .queryParam("type", search.getType())
                        .queryParam("market", "US");

        if (search.getLimit() != null)
            urlBuilder.queryParam("limit", search.getLimit());

        if (search.getOffset() != null)
            urlBuilder.queryParam("offset", search.getOffset());

        String url = urlBuilder.encode().toUriString();
        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidSearchException("Invalid search");
        }

        return responseJson;
    }

    public JsonNode getSong(String id) {
        String urlEnd = "/tracks/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiURL + urlEnd, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidSearchException("Invalid song");
        }

        songRepository.save(new Song(responseJson));

        return responseJson;
    }

    public JsonNode getArtist(String id) {
        String urlEnd = "/artists/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + getAPIToken());

        JsonNode responseJson;

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiURL + urlEnd, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            responseJson = objectMapper.readTree(response.getBody());
        } catch(Exception e) {
            throw new InvalidSearchException("Invalid artist");
        }

        artistRepository.save(new Artist(responseJson));

        return responseJson;
    }
}
