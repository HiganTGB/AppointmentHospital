package appointmenthospital.infoservice.model.dto;

import appointmenthospital.infoservice.model.entity.MedicalSpecialty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String description;
    @NotNull
    private BigDecimal price;

    public MedicalSpecialtyDTO(MedicalSpecialty medicalSpecialty)
    {
        this.id=medicalSpecialty.getId();
        this.name=medicalSpecialty.getName();
        this.description=medicalSpecialty.getDescription();
        this.price=medicalSpecialty.getPrice();
    }
}
