package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Diagnostic extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @OneToMany(mappedBy = "diagnosticService")
    private List<ExaminationDetail> examinationDetails;
    @ManyToOne
    private Doctor doctor;

}
