package appointmenthospital.authservice.model.dto;


import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DoctorFilterRequest {
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("specialties")
    private List<Long> MedicalSpecialtyIdList;
    @JsonProperty("degrees")
    private List<Degree> degree;
}
