package harmonize.DTOs;

import harmonize.Users.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Data
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String bio;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.bio = user.getBio();
    }
}
