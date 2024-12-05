package appointmenthospital.authservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ExaminationDetail extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "diagnostic_service_id")
    private Diagnostic diagnostic;
    @ManyToOne
    @JoinColumn(name = "examination_id")
    private Examination examination;
    private String document;
}
