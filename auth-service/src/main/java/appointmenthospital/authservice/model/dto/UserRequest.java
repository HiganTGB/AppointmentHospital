package appointmenthospital.authservice.model.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "First name is required")

    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")

    private String email;


    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;

}
