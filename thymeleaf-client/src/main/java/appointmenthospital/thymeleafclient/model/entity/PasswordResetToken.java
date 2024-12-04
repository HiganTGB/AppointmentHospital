package appointmenthospital.thymeleafclient.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;
    private Long id;
    private String token;
    private User user;
    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, User user) {
        this.token=token;
        this.user=user;
        this.expiryDate=LocalDateTime.now().plusMinutes(EXPIRATION);
    }
}