package appointmenthospital.thymeleafclient.model.authservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationDTO {
    private long id;
    @JsonProperty("appointment")
    @NotNull(message = "AppointmentID is required")
    private long appointmentId;
    private String diagnostic;
    private String description;
    private int state;
}
