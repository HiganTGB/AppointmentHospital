package appointmenthospital.authservice.model.dto;


import appointmenthospital.authservice.model.entity.Degree;
import lombok.Data;

import java.util.List;

@Data
public class DoctorFilterRequest {
    String gender;
    List<Long> MedicalSpecialtyIdList;
    List<Degree> degree;
}
