package appointmenthospital.authservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Prescription extends BaseEntity{
    @Column
    private String description;
    @OneToOne
    @JoinColumn(name = "examination_id")
    private Examination examination;
    @Column(nullable = true)
    private String document;
}
