package appointmenthospital.authservice.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalSpecialtyDTO {
    protected Long id;
    @NotBlank(message = "Name is required")
    private String name;
}
