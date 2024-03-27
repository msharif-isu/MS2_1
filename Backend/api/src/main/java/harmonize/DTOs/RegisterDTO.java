package harmonize.DTOs;

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
}
