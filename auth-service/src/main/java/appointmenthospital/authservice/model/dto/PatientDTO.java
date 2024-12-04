package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

@Data
public class PatientDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    private String fullName;
 //   private String userName;
 @NotBlank(message = "email is required")
 @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;
    private Long roleId;
    private Long userId;
    public PatientDTO(Patient patient)
    {
        this.id=patient.getId();
        this.fullName=patient.getUser().getFullName();
        this.email=patient.getUser().getEmail();
        this.phone=patient.getUser().getPhone();
        this.roleId=patient.getUser().getRole().getId();
        this.userId=patient.getUser().getId();
    }
}
