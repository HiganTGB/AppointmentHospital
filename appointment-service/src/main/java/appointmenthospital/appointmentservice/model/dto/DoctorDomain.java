package appointmenthospital.appointmentservice.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class DoctorDomain {
    protected Long id;
    private String fullName;
    private List<Long> MedicalSpecialtyIDs;

}
