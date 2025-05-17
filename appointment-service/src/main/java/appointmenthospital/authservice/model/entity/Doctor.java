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
public class Doctor extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private String certificate;
    @Column(nullable = true)
    private String image;
    @OneToMany(mappedBy = "doctor")
    private List<Diagnostic> diagnosticServices;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy = "doctor",targetEntity = ExaminationDetail.class)
    private List<ExaminationDetail> examinationDetails;
}
