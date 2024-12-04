package appointmenthospital.thymeleafclient.model.authservice;

import appointmenthospital.thymeleafclient.model.entity.Permission;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    @JsonProperty("permissions")
    private List<Permission> permissions=new ArrayList<>();
}
