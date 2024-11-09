package appointmenthospital.scheduleservice.model.dto;


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
