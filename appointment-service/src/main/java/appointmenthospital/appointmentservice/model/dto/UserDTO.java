package appointmenthospital.appointmentservice.model.dto;

import lombok.Data;

import java.sql.Timestamp;

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
}
