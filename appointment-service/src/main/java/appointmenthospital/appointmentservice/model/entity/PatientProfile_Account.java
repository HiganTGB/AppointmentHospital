package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile_Account extends BaseEntity{
    private Long accountID;
    @ManyToOne(fetch = FetchType.EAGER)
    private PatientProfile patient;
}
