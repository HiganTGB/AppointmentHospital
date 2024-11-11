package appointmenthospital.scheduleservice.model.dto;

import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleRequest {
    @NotNull(message = "doctor_id")
    @JsonProperty("doctor_id")
    private Long doctorID;
    @NotNull(message = "room_id")
    @JsonProperty("room_id")
    private Long roomID;
    @JsonProperty("at_morning")
    @NotNull(message = "at_morning")
    private Boolean atMorning;
    @NotNull(message = "day of week")
    @JsonProperty("day_of_week")
    private DayOfWeek dayOfWeek;
}
