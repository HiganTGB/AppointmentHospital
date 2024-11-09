package appointmenthospital.thymeleafclient.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalSpecialtyDTO {
    protected Long id;
    private String name;
    private String description;
    private BigDecimal price;

}
