package appointmenthospital.appointmentservice.model.dto;

import appointmenthospital.appointmentservice.model.entity.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class ScheduleDTO {
    private Long id;

    @JsonProperty("doctor_id")
    private Long doctorID;
    @JsonProperty("doctor_name")
    private String doctorName;

    @JsonProperty("room_id")
    private Long roomID;
    @JsonProperty("at_Morning")
    private Boolean atMorning;
    private DayOfWeek dayOfWeek;
}
