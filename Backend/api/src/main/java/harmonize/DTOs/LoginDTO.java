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
public class LoginDTO {
    private String username;
    private String password;
}
