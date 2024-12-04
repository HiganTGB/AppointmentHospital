package appointmenthospital.thymeleafclient.model.authservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private long id;

    private long examinationId;
    private String description;
    private String document;
}
