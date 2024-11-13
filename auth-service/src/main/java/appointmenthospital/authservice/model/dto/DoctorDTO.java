package appointmenthospital.authservice.model.dto;


import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class DoctorDTO {
    protected Long id;
    UserDTO userDTO;

    private Degree degree;

    private Gender gender;
    @JsonProperty("avatar")
    private String urlAvatar;
    private Timestamp createAt;
    private Timestamp updateAt;
    public DoctorDTO(Doctor doctor)
    {
        this.id= doctor.getId();
        this.degree=doctor.getDegree();
        this.gender=doctor.getGender();
        this.createAt=doctor.getCreateAt();
        this.updateAt=doctor.getUpdateAt();
        this.urlAvatar=doctor.getUrlAvatar();
        UserDTO userDTO=new UserDTO();
        this.userDTO= userDTO.getFromEntity(doctor.getUser());
    }
}
