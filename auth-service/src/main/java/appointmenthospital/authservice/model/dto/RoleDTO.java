package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Permission;
import appointmenthospital.authservice.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    @NotBlank(message = "Name is required")

    private String name;

    private String description;
    @JsonProperty("permissions")
    private List<Permission> permissions=new ArrayList<>();
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
