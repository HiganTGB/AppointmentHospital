package appointmenthospital.infoservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Room extends BaseEntity{
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description",nullable = true)
    private String description;
    @ManyToOne(targetEntity = MedicalSpecialty.class)
    private MedicalSpecialty medicalSpecialty;
    @OneToMany(mappedBy = "room",targetEntity = Schedule.class)
    Set<Schedule> schedules;
}
