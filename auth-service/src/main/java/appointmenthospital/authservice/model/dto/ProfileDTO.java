package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Gender;
import appointmenthospital.authservice.model.entity.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@NoArgsConstructor
public class ProfileDTO {
    private long id;
    @NotNull(message = "patient not found")
    private long patientId;
    @JsonProperty("full_name")
    @NotNull(message = "full_name not valid")
    private String fullName;
    @JsonProperty("date_of_birth")
    @NotNull(message = "date_of_birth not valid")
    private LocalDate dateOfBirth;
    @NotNull(message = "gender not valid")
    private Gender gender;
    public ProfileDTO(Profile profile)
    {
        this.id=profile.getId();
        this.patientId=profile.getPatient().getId();
        this.fullName=profile.getFullName();
        this.gender=profile.getGender();
        this.dateOfBirth=profile.getDateOfBirth();
    }
}
