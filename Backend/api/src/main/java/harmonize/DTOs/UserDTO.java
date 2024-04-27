package harmonize.DTOs;

import java.util.HashSet;
import java.util.Set;

import harmonize.Entities.Role;
import harmonize.Entities.User;
import lombok.Data;
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
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String bio;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return user.id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
