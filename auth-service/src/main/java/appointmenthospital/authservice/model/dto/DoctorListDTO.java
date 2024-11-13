package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Degree;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class DoctorListDTO {
    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    private Gender gender;
    private Degree degree;
    @JsonProperty("url_avatar")
    private String urlAvatar;
    public DoctorListDTO(Doctor doctor)
    {
        this.id= doctor.getId();
        this.degree=doctor.getDegree();
        String fullName= String.format("%s. %s",doctor.getDegree(),doctor.getFullName());
        this.fullName=fullName.trim();
        this.gender=doctor.getGender();
        this.urlAvatar=doctor.getUrlAvatar();
        UserDTO userDTO=new UserDTO();
    }
}
