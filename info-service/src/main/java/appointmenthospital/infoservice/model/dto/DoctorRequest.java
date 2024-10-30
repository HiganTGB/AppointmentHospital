package appointmenthospital.infoservice.model.dto;

import appointmenthospital.infoservice.model.entity.Degree;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class DoctorRequest {
    @Valid
    @NotNull(message = "Info is required")
    private UserRequest userRequest;
    @NotNull(message = "Degree is required")
    private Degree degree;

    @NotNull(message = "Gender is required")
    private boolean gender;
    @NotNull(message = "Medical Specialty is required")
    @NotEmpty(message = "Medical Specialty is required")
    @Size(min = 1, max = 2,message = "Medical Specialty is required (min = 2, max = 1)")
    private List<Long> MedicalSpecialtiesId;
}
