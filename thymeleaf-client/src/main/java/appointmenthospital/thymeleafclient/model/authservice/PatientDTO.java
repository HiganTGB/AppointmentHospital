package appointmenthospital.thymeleafclient.model.authservice;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private long id;
    @NotBlank(message = "Name is required")
    private String fullName;
    @NotBlank(message = "email is required")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;
    private long roleId;
    private long userId;
}
