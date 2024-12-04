package appointmenthospital.thymeleafclient.model.authservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDTO {
    private long id;
    private String name;
    private String image;
    private String unit;
    private String description;
}
