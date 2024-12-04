package appointmenthospital.thymeleafclient.model.entity;

import appointmenthospital.thymeleafclient.model.authservice.AppointmentDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Patient patient;
    private List<AppointmentDTO> appointment;
}
