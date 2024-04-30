package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Role;
import lombok.Data;

@Data
public class RoleDTO {
    private int id;
    private String name;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    @JsonCreator
    public RoleDTO(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}

