package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Appointment extends BaseEntity{
    @Column(nullable = false)
    int number;
    @Column(nullable = false)
    LocalDateTime atTime;
    @Column(nullable = false)
    int state;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @OneToOne
    @JoinColumn(name = "examination_id")
    private Examination examination;
    @Column(nullable = false)
    private BigDecimal price;
}
