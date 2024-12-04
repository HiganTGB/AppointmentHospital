package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Gender;
import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class DoctorDTO {
    private Long id;
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
    private Long roleId;
    private Long userId;
    public DoctorDTO(Doctor doctor)
    {
        this.id=doctor.getId();
        User user = doctor.getUser();
        this.fullName= user.getFullName();
        this.email= user.getEmail();
        this.phone= user.getPhone();
        this.position=doctor.getPosition();
        this.certificate=doctor.getCertificate();
        Role role = user.getRole();
        this.roleId= role == null ? null : role.getId();
        this.gender=doctor.getGender();
        this.userId= user.getId();
    }
}
