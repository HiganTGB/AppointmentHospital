package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.ExaminationDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class ExaminationDiagnosticDTO {
    private String name;
    private BigDecimal price;
    private long doctorId;
    private long diagnosticServiceId;
    private long examinationId;
    public ExaminationDiagnosticDTO(ExaminationDetail examinationDetail)
    {
        this.name=examinationDetail.getDiagnostic().getName();
        this.price=examinationDetail.getDiagnostic().getPrice();
        this.examinationId=examinationDetail.getExamination().getId();
        this.diagnosticServiceId=examinationDetail.getDiagnostic().getId();
    }
}
