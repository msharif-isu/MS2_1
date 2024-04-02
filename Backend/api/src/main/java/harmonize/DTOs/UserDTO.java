package harmonize.DTOs;

import java.util.HashSet;
import java.util.Set;

import harmonize.Entities.Role;
import harmonize.Entities.User;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@Data
@RequiredArgsConstructor
public class UserDTO {
    private final int id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String username;

    @NonNull
    private String bio;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.bio = user.getBio();
        for (Role role : user.getRoles()) {
            this.roles.add(new RoleDTO(role));
        }
    }
}
