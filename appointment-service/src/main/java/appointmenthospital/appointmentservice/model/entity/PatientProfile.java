package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.*;
import jakarta.ws.rs.core.Link;
import lombok.*;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile extends DataInfoTime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false,name = "fullName")
    private String fullName;
    @Column(name = "gender",nullable = false)
    private Gender gender;
    @Column(nullable = false,name = "identityCard")
    private String identityCard;
    @Column(nullable = false,name = "phone")
    private String phone;
    @Column(nullable = true,name = "email")
    private String email;
    @Column(nullable = false,name = "address")
    private String address;
    @OneToMany(mappedBy = "patientProfile",targetEntity = Examination.class,fetch = FetchType.LAZY)
    private List<Examination> examinations;
    @OneToMany(mappedBy = "patient",targetEntity = PatientProfile_Account.class,fetch = FetchType.LAZY)
    private List<PatientProfile_Account> patientProfile_accounts;
}
