package appointmenthospital.thymeleafclient.model;

import appointmenthospital.authservice.model.entity.Medicine;
import lombok.Data;

@Data
public class MedicineDTO {
    private long id;
    private String name;
    private String image;
    private String unit;
    private String description;
    public MedicineDTO(Medicine medicine)
    {
        this.id=medicine.getId();
        this.name=medicine.getName();
        this.image=medicine.getImage();
        this.unit=medicine.getUnit();
        this.description=medicine.getDescription();
    }
}
