package appointmenthospital.authservice.model.dto;


import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Doctor;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class DoctorDTO {
    protected Long id;
    UserDTO userDTO;

    private Degree degree;

    private boolean gender;
    private Timestamp createAt;

    private Timestamp updateAt;
    public DoctorDTO(Doctor doctor)
    {
        this.id= doctor.getId();
        this.degree=doctor.getDegree();
        this.gender=doctor.isGender();
        this.createAt=doctor.getCreateAt();
        this.updateAt=doctor.getUpdateAt();
        UserDTO userDTO=new UserDTO();
        this.userDTO= userDTO.getFromEntity(doctor.getUser());
    }
}
