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
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @JsonCreator
    public RegisterDTO(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
}
