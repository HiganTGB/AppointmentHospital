package appointmenthospital.authservice.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstname;
    @NotBlank(message = "Last name is required")
    private String lastname;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;
    @NotBlank(message = "Password is required")
    private String password;
}