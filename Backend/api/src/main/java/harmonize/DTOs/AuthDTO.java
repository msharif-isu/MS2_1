package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Data
public class AuthDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonCreator
    public AuthDTO(@JsonProperty("accessToken") String accessToken, @JsonProperty("tokenType") String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}
