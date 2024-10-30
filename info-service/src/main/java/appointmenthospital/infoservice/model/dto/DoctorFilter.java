package appointmenthospital.infoservice.model.dto;

import appointmenthospital.infoservice.model.entity.Degree;
import lombok.Data;

import java.util.List;
@Data
public class DoctorFilter {
    String keyword;
    String gender;
    List<Long> MedicalSpecialtyIdList;
    List<Degree> degree;
}
