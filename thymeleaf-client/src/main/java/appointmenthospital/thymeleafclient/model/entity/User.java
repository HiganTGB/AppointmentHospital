package appointmenthospital.thymeleafclient.model.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User{
    private String password;
    private String fullName;
    private String email;
    private Boolean emailVerified=false;
    private String phone;
    private Boolean phoneVerified=false;
    private Boolean isStaff;
    private boolean isEnabled;
    private String image;
}
