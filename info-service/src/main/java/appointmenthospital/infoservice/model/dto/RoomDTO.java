package appointmenthospital.infoservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomDTO {
    protected Long id;
    @NotBlank(message = "Name is required")

    private String name;

    private String description;

    private Long medicalSpecialtyId;
}
