package harmonize.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Data
@AllArgsConstructor
public class AuthDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
