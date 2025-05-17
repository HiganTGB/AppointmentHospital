package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Examination extends BaseEntity{
    private String diagnostic;
    private String description;
    private int state;
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    @OneToMany(mappedBy = "examination")
    private List<ExaminationDetail> examinationDetails;
    @OneToOne(mappedBy = "examination",targetEntity = Prescription.class,fetch = FetchType.EAGER)
    private Prescription prescription;

}
