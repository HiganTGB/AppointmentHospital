package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.ws.rs.core.Link;

import java.util.Date;
import java.util.List;

@Entity
public class PatientProfile extends BaseEntity {
    @Column(nullable = false,name = "password")
    private String password;
    @Column(nullable = false,name = "firstName")
    private String firstName;
    @Column(name = "gender",nullable = false)
    private boolean gender;
    @Column(nullable = false,name = "identityCard")
    private String identityCard;
    @Column(nullable = false,name = "phone")
    private String phone;
    @Column(nullable = false,name = "email")
    private String email;
    @Column(nullable = false,name = "address")
    private String address;
    @Column(nullable = false,name = "account_id",updatable = false)
    private Long account_id;
    @OneToMany(mappedBy = "patientProfile",targetEntity = Examination.class)
    private List<Examination> examinations;
}
