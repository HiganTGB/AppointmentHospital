package appointmenthospital.infoservice.model.dto;

import appointmenthospital.infoservice.model.entity.Degree;
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

}
