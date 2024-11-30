package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.MedicineDTO;
import appointmenthospital.authservice.model.dto.RoleDTO;
import appointmenthospital.authservice.model.entity.Medicine;
import appointmenthospital.authservice.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private MedicineService medicineService;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MedicineDTO get(@PathVariable Long id)
    {
        return medicineService.get(id);
    }
}
