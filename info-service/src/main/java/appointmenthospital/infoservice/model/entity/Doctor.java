package appointmenthospital.infoservice.model.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table
public class Doctor extends BaseEntity {
    @Column
    @Enumerated(value = EnumType.STRING)
    private Degree degree;
    @Column(name = "gender")
    private boolean gender;
    @ManyToMany
    @JoinTable(
            name = "doctor_specialty",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private List<MedicalSpecialty> doctorSpecialty;
    @OneToMany(mappedBy = "doctor",targetEntity = Schedule.class)
    Set<Schedule> schedules;
    @Column(unique = true,nullable = true,updatable = false)
    private Long account_id;

}
