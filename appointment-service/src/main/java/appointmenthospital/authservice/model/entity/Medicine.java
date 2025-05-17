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
public class Medicine extends BaseEntity {
    private String name;
    private String image;
    private String unit;
    private String description;
    @OneToMany(mappedBy = "medicine",fetch = FetchType.LAZY)
    private List<PrescriptionDetail> prescriptionDetails;
}
