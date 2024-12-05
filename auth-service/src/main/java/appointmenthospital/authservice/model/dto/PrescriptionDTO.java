package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Prescription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrescriptionDTO {
    private long id;

    private long examinationId;
    private String description;
    private String document;

    public PrescriptionDTO(Prescription prescription) {
        this.id=prescription.getId();
        this.examinationId=prescription.getExamination().getId();
        this.description=prescription.getDescription();
        this.document=prescription.getDocument();
    }
}
