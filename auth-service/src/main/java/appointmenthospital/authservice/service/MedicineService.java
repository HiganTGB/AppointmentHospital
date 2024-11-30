package appointmenthospital.authservice.service;

import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.MedicineDTO;
import appointmenthospital.authservice.model.entity.Medicine;
import appointmenthospital.authservice.repository.MedicineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private MedicineRepository medicineRepository;
    private CustomLogger logger;
    public MedicineDTO get(long id)
    {
        return new MedicineDTO(getEntity(id));
    }
    public Medicine getEntity(long id)
    {
        return medicineRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Medicine with " + id + "not found" ));
    }
}
