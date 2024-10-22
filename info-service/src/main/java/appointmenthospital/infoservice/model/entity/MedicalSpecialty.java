package appointmenthospital.infoservice.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MedicalSpecialty extends BaseEntity
{
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description",nullable = true)
    private String description;
    @ManyToMany(mappedBy = "doctorSpecialty")
    private List<Doctor> doctors;
    @OneToMany(mappedBy = "medicalSpecialty",targetEntity = Room.class)
    private List<Room> room;
}
