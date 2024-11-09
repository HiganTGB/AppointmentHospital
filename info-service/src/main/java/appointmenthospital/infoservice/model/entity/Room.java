package appointmenthospital.infoservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room extends BaseEntity{
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description",nullable = true)
    private String description;
    @ManyToOne(targetEntity = MedicalSpecialty.class,fetch = FetchType.LAZY)
    private MedicalSpecialty medicalSpecialty;
}
