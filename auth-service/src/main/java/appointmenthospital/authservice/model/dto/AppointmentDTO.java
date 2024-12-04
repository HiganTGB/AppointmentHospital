package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
public class AppointmentDTO {
    private long id;
    @JsonProperty("at")
    private LocalDateTime atTime;
    private long number;
    private long state;
    private Long profile;
    @JsonProperty("doctor")
    private long doctorId;
    private BigDecimal price;
    @Setter
    @JsonProperty("payment_url")
    private String payment;
    public AppointmentDTO(Appointment appointment)
    {
        this.id=appointment.getId();
        this.atTime=appointment.getAtTime();
        this.number=appointment.getNumber();
        this.state=appointment.getState();
        this.profile=appointment.getProfile().getId();
        this.doctorId=appointment.getDoctor().getId();
        this.price=appointment.getPrice();
    }
}
