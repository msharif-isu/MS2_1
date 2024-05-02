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
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.ConversationDTO;
import harmonize.DTOs.ReportDTO;
import harmonize.DTOs.ResponseDTO;
import harmonize.DTOs.RoleDTO;
import harmonize.DTOs.UserDTO;

@Service
public class RequestService {

    @Autowired
    private TestRestTemplate testRestTemplate;

    public ResponseEntity<AuthDTO> requestAuth(String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return testRestTemplate.exchange(
            path,
            HttpMethod.POST,
            new HttpEntity<>(headers),
            AuthDTO.class);
    }

    public ResponseEntity<AuthDTO> requestAuth(String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return testRestTemplate.exchange(
            path,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            AuthDTO.class);
    }

    public ResponseEntity<UserDTO> requestUser(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            UserDTO.class);
    }

    public ResponseEntity<UserDTO> requestUser(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            UserDTO.class);
    }

    public ResponseEntity<ReportDTO> requestReport(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            ReportDTO.class);
    }

    public ResponseEntity<ReportDTO> requestReport(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            ReportDTO.class);
    }

    public ResponseEntity<String> requestString(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            String.class);
    }

    public ResponseEntity<String> requestString(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            String.class);
    }

    public ResponseEntity<ConversationDTO> requestConversation(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<ConversationDTO>() {});
    }

    public ResponseEntity<ConversationDTO> requestConversation(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            new ParameterizedTypeReference<ConversationDTO>() {});
    }

    public ResponseEntity<List<UserDTO>> requestUserList(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<UserDTO>>() {});
    }

    public ResponseEntity<List<UserDTO>> requestUserList(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            new ParameterizedTypeReference<List<UserDTO>>() {});
    }

    public ResponseEntity<List<RoleDTO>> requestRolesList(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<RoleDTO>>() {});
    }

    public ResponseEntity<List<RoleDTO>> requestRolesList(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            new ParameterizedTypeReference<List<RoleDTO>>() {});
    }

    public ResponseEntity<List<ReportDTO>> requestReports(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<ReportDTO>>() {});
    }

    public ResponseEntity<List<ReportDTO>> requestReports(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            new ParameterizedTypeReference<List<ReportDTO>>() {});
    }
    
    public ResponseEntity<ResponseDTO> requestResponse(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            ResponseDTO.class);
    }

    public ResponseEntity<ResponseDTO> requestResponse(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            ResponseDTO.class);
    }

    public ResponseEntity<JsonNode> requestJson(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            JsonNode.class);
    }

    public ResponseEntity<JsonNode> requestJson(AuthDTO auth, String path, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(body, headers),
            JsonNode.class);
    }

    public ResponseEntity<byte[]> requestByteArray(AuthDTO auth, String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return testRestTemplate.exchange(
            path,
            method,
            new HttpEntity<>(headers),
            byte[].class);
    }

    public ResponseEntity<byte[]> requestByteArray(AuthDTO auth, String path, HttpMethod method, HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        return testRestTemplate.exchange(
            path,
            method,
            requestEntity,
            byte[].class);
    }
}
