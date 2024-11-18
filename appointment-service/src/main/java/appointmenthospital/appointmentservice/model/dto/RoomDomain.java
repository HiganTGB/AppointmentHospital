package appointmenthospital.appointmentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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


}
