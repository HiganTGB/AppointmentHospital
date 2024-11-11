package appointmenthospital.infoservice.model.dto;



import appointmenthospital.infoservice.model.entity.Degree;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class DoctorDomain {
    protected Long id;
    private String fullName;
    private List<Long> MedicalSpecialtyIDs;

}
