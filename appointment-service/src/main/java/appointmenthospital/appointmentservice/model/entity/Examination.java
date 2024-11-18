package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examination extends BaseEntity {
    @Column(nullable = false)
    private long roomID;
    @Column(nullable = false)
    private long specialtyID;
    @Column(nullable = false)
    private long doctorId;
    @ManyToOne(fetch = FetchType.EAGER)
    private PatientProfile patientProfile;
    @Column(nullable = false)
    private long accountID;
    @Column(nullable = false)
    private String roomName;
    @Column(nullable = false)
    private String specialtyName;
    @Column(nullable = false)
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlot timeSlot;

    @Column(nullable = false)
    private int number;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
    @Column(nullable = false)
    private BigDecimal price;
}
