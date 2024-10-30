package appointmenthospital.infoservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MedicalSpecialty extends BaseEntity
{
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description",nullable = true)
    private String description;
    @OneToMany(mappedBy = "medicalSpecialty")
    private List<Room> room;
    @Column(precision = 19, scale = 2,nullable = false)
    private BigDecimal price;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Specialty_Doctor> specialtyDoctors;
}
