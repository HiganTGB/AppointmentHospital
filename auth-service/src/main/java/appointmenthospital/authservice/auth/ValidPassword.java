package appointmenthospital.authservice.auth;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Mật khẩu phải chứa ít nhất một chữ cái in hoa, một chữ cái in thường và một chữ số";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}