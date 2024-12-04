package appointmenthospital.thymeleafclient.model.authservice;

import appointmenthospital.thymeleafclient.model.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    private long id;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;
    private String position;
    private String certificate;
    @NotNull(message = "Gender is required")
    private Gender gender;
    private long roleId;
    private long userId;
}
