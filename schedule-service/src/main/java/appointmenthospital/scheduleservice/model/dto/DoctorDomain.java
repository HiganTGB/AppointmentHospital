package appointmenthospital.scheduleservice.model.dto;


import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class DoctorDomain {
    protected Long id;
    private List<Long> MedicalSpecialtyIDs;

}
