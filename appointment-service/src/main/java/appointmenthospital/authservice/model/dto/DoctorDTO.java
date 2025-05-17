package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
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
    public DoctorDTO(Doctor doctor)
    {
        this.id=doctor.getId();
        this.fullName=doctor.getUser().getFullName();
        this.email=doctor.getUser().getEmail();
        this.phone=doctor.getUser().getPhone();
        this.position=doctor.getPosition();
        this.certificate=doctor.getCertificate();
        this.roleId=doctor.getUser().getRole().getId();
        this.gender=doctor.getGender();
        this.userId=doctor.getUser().getId();
    }
}
