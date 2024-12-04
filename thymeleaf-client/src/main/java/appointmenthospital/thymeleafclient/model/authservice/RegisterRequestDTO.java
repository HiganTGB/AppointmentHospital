package appointmenthospital.thymeleafclient.model.authservice;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String otp;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;

    private String fullName;
    private String password;
    private String email;
}
