package harmonize.DTOs;

import harmonize.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDTO {
    private int id;
    private String name;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }
}

