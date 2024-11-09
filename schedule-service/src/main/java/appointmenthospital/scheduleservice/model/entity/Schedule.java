package appointmenthospital.scheduleservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Schedule extends BaseEntity{

    @Column(nullable = false)
    private Long doctorID;
    @Column(nullable = false)
    private Long roomID;
    @Column(nullable = false)
    private Boolean isDay;
    private DayOfWeek dayOfWeek;
}
