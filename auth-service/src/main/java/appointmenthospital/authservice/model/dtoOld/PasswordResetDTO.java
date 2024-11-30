package appointmenthospital.authservice.model.dtoOld;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String token;
    private String newPassword;
}
