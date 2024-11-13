package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private Gender gender;
    @NotNull(message = "Medical Specialty is required")
    @NotEmpty(message = "Medical Specialty is required")
    @Size(min = 1, max = 2,message = "Medical Specialty is required (min = 1, max = 2)")
    private List<Long> MedicalSpecialtiesId;
}
