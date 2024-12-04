package appointmenthospital.thymeleafclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
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

    public enum Gender {
        F,
        M;
    }
}


