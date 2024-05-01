package harmonize.DTOs;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public UserDTO(
        @JsonProperty("id") int id,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("username") String username,
        @JsonProperty("bio") String bio,
        @JsonProperty("roles") Set<RoleDTO> roles
        ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.bio = bio;
        this.roles = roles;
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
