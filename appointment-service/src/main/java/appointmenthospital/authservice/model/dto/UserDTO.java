package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("full_name")
    @NotBlank(message = "Full name is required")
    private String fullName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String phone;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("phone_verified")
    private Boolean phoneVerified;
    @JsonProperty("is_staff")
    private Boolean isStaff;
    @JsonProperty("enable")
    private boolean isEnabled;
    @JsonProperty("image")
    private String image;

//    private Timestamp lastPasswordResetDate;

 //   private String roleName;
//
//    private Timestamp createAt;
//    private Timestamp updateAt;
    public UserDTO (User user)
    {
        this.id=user.getId();
        this.fullName=user.getFullName();
        this.email=user.getEmail();
        this.emailVerified=user.getEmailVerified();
        this.phoneVerified=user.getPhoneVerified();
        this.phone=user.getPhone();
        this.isStaff=user.getIsStaff();
        this.isEnabled=user.isEnabled();
    }
}
