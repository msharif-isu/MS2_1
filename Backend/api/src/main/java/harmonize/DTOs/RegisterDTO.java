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
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
