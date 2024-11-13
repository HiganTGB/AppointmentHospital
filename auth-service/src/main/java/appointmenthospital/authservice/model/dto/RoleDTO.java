package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Permission;
import appointmenthospital.authservice.model.entity.Role;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;
@Data
public class RoleDTO {
    private Long id;

    private String name;

    private String description;
    private List<Permission> permissions;
    public RoleDTO(Role role)
    {
        this.id=role.getId();
        this.name=role.getName();
        this.description=role.getDescription();
        for(Permission p: Permission.values())
        {
            if(role.isPermissionGranted(p.getCode()))
                this.permissions.add(p);
        }
    }
}
