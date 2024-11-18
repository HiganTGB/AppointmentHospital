package appointmenthospital.scheduleservice.model.domain;


import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class DoctorDomain {
    protected Long id;
    private String fullName;
    private List<Long> MedicalSpecialtyIDs;

}
