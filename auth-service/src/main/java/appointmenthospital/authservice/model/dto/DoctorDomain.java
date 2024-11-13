package appointmenthospital.authservice.model.dto;


import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Doctor_Specialty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class DoctorDomain {
    protected Long id;
    private String fullName;
    private List<Long> MedicalSpecialtyIDs;
    public DoctorDomain(Doctor doctor)
    {
        this.fullName= doctor.getFullName();
        this.id= doctor.getId();
        this.MedicalSpecialtyIDs=(doctor.getDoctorSpecialties()!=null) ?doctor.getDoctorSpecialties().stream().map(Doctor_Specialty::getSpecialtyId).toList():null;
    }
}
