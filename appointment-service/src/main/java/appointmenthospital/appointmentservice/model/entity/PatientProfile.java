package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class PatientProfile extends BaseEntity {
    @Column(nullable = false,name = "fullName")
    private String fullName;
    @Column(name = "gender",nullable = false)
    private boolean gender;
    @Column(nullable = false,name = "identityCard")
    private String identityCard;
    @Column(nullable = false,name = "phone")
    private String phone;
    @Column(nullable = true,name = "email")
    private String email;
    @Column(nullable = false,name = "address")
    private String address;
    @Column(nullable = true,name = "account_id",updatable = false)
    private Long account_id;
    @OneToMany(mappedBy = "patientProfile",targetEntity = Examination.class)
    private List<Examination> examinations;
    @Column(nullable = true)
    private String patientUUID;
}
