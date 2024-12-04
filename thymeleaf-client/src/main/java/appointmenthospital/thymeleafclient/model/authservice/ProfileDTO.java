package appointmenthospital.thymeleafclient.model.authservice;

import appointmenthospital.thymeleafclient.model.entity.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
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
}
