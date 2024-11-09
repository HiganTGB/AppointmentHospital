package appointmenthospital.scheduleservice.model.dto;

import appointmenthospital.scheduleservice.model.entity.BaseEntity;
import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleRequest {
    @NotNull
    private Long doctorID;
    @NotNull
    private Long roomID;
    @NotNull
    private Boolean isDay;
    @NotNull
    private DayOfWeek dayOfWeek;
}
