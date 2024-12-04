package appointmenthospital.thymeleafclient.model.authservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private long id;
    @JsonProperty("at")
    private LocalDateTime atTime;
    private long number;
    private long state;
    private Long profile;
    @JsonProperty("doctor")
    private long doctorId;
}
