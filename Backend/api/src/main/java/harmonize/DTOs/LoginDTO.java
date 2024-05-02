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
public class LoginDTO {
    private String username;
    private String password;

    @JsonCreator
    public LoginDTO(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }
}
