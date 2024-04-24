package harmonize.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;

@Service
public class RequestService {

    @Autowired
    private TestRestTemplate restTemplate;

    public ResponseEntity<String> requestWelcome(String path) {
        return restTemplate.exchange(
            path,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            String.class);
    }

    public ResponseEntity<AuthDTO> requestAuth(String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(
            path,
            HttpMethod.POST,
            new HttpEntity<>(headers),
            AuthDTO.class);
    }

    public ResponseEntity<AuthDTO> requestAuth(String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(
            path,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            AuthDTO.class);
    }

    public ResponseEntity<UserDTO> requestUser(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            UserDTO.class);
    }

    public ResponseEntity<UserDTO> requestUser(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            UserDTO.class);
    }

    public ResponseEntity<String> requestString(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            String.class);
    }

    public ResponseEntity<String> requestString(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            String.class);
    }

    public ResponseEntity<List<UserDTO>> requestUserList(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<UserDTO>>() {});
    }

    public ResponseEntity<List<UserDTO>> requestUserList(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            new ParameterizedTypeReference<List<UserDTO>>() {});
    }

    public ResponseEntity<List<RoleDTO>> requestRolesList(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<RoleDTO>>() {});
    }

    public ResponseEntity<List<RoleDTO>> requestRolesList(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return restTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            new ParameterizedTypeReference<List<RoleDTO>>() {});
    }
    
}
