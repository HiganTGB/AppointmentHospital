package appointmenthospital.scheduleservice.model.entity;

import jakarta.persistence.*;
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
    private Boolean atMorning;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private int dayOfWeekValue;
    @PrePersist
    @PreUpdate
    public void updateDayOfWeekValue() {
        this.dayOfWeekValue = dayOfWeek.getValue();
    }
}
