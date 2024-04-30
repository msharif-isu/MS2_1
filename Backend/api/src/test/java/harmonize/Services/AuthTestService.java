package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;

import harmonize.DTOs.AuthDTO;
import harmonize.DTOs.LoginDTO;
import harmonize.DTOs.RegisterDTO;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthTestService {
    private int port;
    private String url;

    @Autowired
    private RequestService requestService;

    public void setConnection(String hostname, int port) {
        this.url = "https://" + hostname + ":";
        this.port = port;
    }

    public ResponseEntity<AuthDTO> register(RegisterDTO user) {
        return requestService.requestAuth(url + port + "/auth/register", HttpMethod.POST, user);
    }

    public ResponseEntity<AuthDTO> login(LoginDTO user) {
        return requestService.requestAuth(url + port + "/auth/login", HttpMethod.POST, user);
    }
}
