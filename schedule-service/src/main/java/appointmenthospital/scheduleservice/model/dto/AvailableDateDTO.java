package appointmenthospital.scheduleservice.model.dto;

import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailableDateDTO {
    private int dayOfWeek;
    private boolean Available;
}
