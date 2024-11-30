package appointmenthospital.authservice.auth;

import appointmenthospital.authservice.model.dtoOld.OtpDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends OtpDTO {
    @NotBlank(message = "Name is required")
    private String fullName;
    @NotBlank(message = "Password is required")
    private String password;
    @Email(message = "Required email")
    private String email;
}