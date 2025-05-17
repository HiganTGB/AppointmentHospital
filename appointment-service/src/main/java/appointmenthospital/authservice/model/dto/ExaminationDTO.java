package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Examination;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExaminationDTO {
    private long id;
    @JsonProperty("appointment")
    @NotNull(message = "AppointmentID is required")
    private long appointmentId;
    private String diagnostic;
    private String description;
    private int state;
    public ExaminationDTO(Examination examination)
    {
        this.id=examination.getId();
        this.appointmentId =examination.getAppointment().getId();
        this.diagnostic=examination.getDiagnostic();
        this.description=examination.getDescription();
        this.state=examination.getState();
    }
}
