package appointmenthospital.infoservice.model.dto;

import appointmenthospital.infoservice.model.entity.Room;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDomain {
    protected Long id;

    private String name;

    @JsonProperty("specialty_id")
    private Long medicalSpecialtyId;
    @Null
    @JsonProperty("specialty_name")
    private String medicalSpecialtyName;
    public RoomDomain(Room room)
    {
        this.id=room.getId();
        this.name=room.getName();
        this.medicalSpecialtyId=room.getMedicalSpecialty().getId();
        this.medicalSpecialtyName=room.getMedicalSpecialty().getName();
    }


}
