package appointmenthospital.authservice.model.dto;

import appointmenthospital.authservice.model.entity.Diagnostic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DiagnosticDTO {

    private long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Price is required")
    private BigDecimal price;
    public DiagnosticDTO(Diagnostic diagnostic)
    {
        this.id= diagnostic.getId();
        this.name= diagnostic.getName();
        this.price= diagnostic.getPrice();
    }
}
