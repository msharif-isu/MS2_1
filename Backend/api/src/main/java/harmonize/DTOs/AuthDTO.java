package harmonize.DTOs;

import lombok.Data;

@Data
public class AuthDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
