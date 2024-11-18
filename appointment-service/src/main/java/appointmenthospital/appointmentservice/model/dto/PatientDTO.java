package appointmenthospital.appointmentservice.model.dto;

import appointmenthospital.appointmentservice.model.entity.Gender;
import appointmenthospital.appointmentservice.model.entity.PatientProfile;
import appointmenthospital.appointmentservice.model.entity.PatientProfile_Account;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class PatientDTO {
    private String fullName;

    private Gender gender;

    private String identityCard;

    private String phone;

    private String email;
    private String address;

    private String id;
    public PatientDTO(PatientProfile profile)
    {
        this.fullName=profile.getFullName();
        this.gender=profile.getGender();
        this.identityCard=profile.getIdentityCard();
        this.phone=profile.getPhone();
        this.email=profile.getEmail();
        this.address=profile.getAddress();
        this.id=profile.getId();
    }
}
