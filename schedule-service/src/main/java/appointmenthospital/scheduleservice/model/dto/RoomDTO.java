package appointmenthospital.scheduleservice.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDTO {
    protected Long id;
    @NotBlank(message = "Name is required")

    private String name;

    private String description;
    @NotNull(message = "Medical Specialty is required")
    private Long medicalSpecialtyId;
    @Null
    private MedicalSpecialtyDTO medicalSpecialty;



}
