package appointmenthospital.infoservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder


public class Specialty_Doctor extends BaseEntity{
    @ManyToOne
    private MedicalSpecialty medicalSpecialty;
    private long Doctor_id;


}
