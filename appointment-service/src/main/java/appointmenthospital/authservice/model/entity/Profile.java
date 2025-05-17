package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Profile extends BaseEntity {
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
    @OneToMany(mappedBy = "profile",targetEntity = Appointment.class)
    private List<Appointment> appointment;
}
