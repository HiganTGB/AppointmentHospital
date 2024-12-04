package appointmenthospital.thymeleafclient.model;

import appointmenthospital.authservice.model.entity.Diagnostic;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DiagnosticDTO {
    @NotNull(message = "Doctor ID is required.")
    private long id;
    private String name;
    private BigDecimal price;
    public DiagnosticDTO(Diagnostic diagnostic)
    {
        this.id= diagnostic.getId();
        this.name= diagnostic.getName();
        this.price= diagnostic.getPrice();
    }
}
