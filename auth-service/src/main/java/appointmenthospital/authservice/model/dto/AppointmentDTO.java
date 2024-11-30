package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class AppointmentDTO {
    private long id;
    @JsonProperty("at")
    private LocalDateTime atTime;
    private long number;
    private long state;
    private Long profile;
    private long doctor;
    public AppointmentDTO(Appointment appointment)
    {
        this.id=appointment.getId();
        this.atTime=appointment.getAtTime();
        this.number=appointment.getNumber();
        this.state=appointment.getState();
        this.profile=appointment.getProfile().getId();
        this.doctor=appointment.getDoctor().getId();

    }
}
