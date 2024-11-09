package appointmenthospital.authservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Doctor_Specialty extends DataInfoTime {
    @ManyToOne
    private Doctor doctor;
    private Long SpecialtyId;
    @Id
    private Long id;
}
