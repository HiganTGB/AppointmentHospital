package appointmenthospital.scheduleservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Schedule extends BaseEntity{

    @Column(nullable = false)
    private Long doctor_id;
    @Column(nullable = false)
    private Long room_id;
    @Column(nullable = false)
    private Boolean isDay;
}
