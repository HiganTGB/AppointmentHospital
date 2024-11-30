package appointmenthospital.authservice.model.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.*;

@JsonAutoDetect
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class PrescriptionDetail extends BaseEntity {
    @ManyToOne
    @Column(name = "prescription_id")
    private Prescription prescription;
    @ManyToOne
    @Column(name = "medicine_id")
    private Medicine medicine;
    @Column
    private String description;
}
