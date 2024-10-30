package appointmenthospital.authservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Doctor_Specialty extends BaseEntity {
    @ManyToOne
    private Doctor doctor;
    private Long SpecialtyId;
}
