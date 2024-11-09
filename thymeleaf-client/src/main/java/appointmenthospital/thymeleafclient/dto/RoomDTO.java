package appointmenthospital.thymeleafclient.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDTO {
    protected Long id;

    private String name;

    private String description;
    private Long medicalSpecialtyId;
    private MedicalSpecialtyDTO medicalSpecialty;
}
