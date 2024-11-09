package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Role;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.token.Token;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.List;
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailVerified;

    private String phone;

    private Boolean phoneVerified;

    private Boolean isStaff;

    private boolean isEnabled;

    private Timestamp lastPasswordResetDate;

    private String roleName;

    private Timestamp createAt;
    private Timestamp updateAt;
    public UserDTO getFromEntity(User user)
    {
        this.id=user.getId();
        this.firstName=user.getFirstName();
        this.lastName=user.getLastName();
        this.email=user.getEmail();
        this.emailVerified=user.getEmailVerified();
        this.phoneVerified=user.getPhoneVerified();
        this.phone=user.getPhone();
        this.isStaff=user.getIsStaff();
        this.isEnabled=user.isEnabled();

        return this;
    }
}
