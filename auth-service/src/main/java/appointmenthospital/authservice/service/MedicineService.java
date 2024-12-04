package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.ExaminationDTO;
import appointmenthospital.authservice.model.dto.MedicineDTO;
import appointmenthospital.authservice.model.entity.Appointment;
import appointmenthospital.authservice.model.entity.Examination;
import appointmenthospital.authservice.model.entity.Medicine;
import appointmenthospital.authservice.model.entity.QMedicine;
import appointmenthospital.authservice.repository.MedicineRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private MedicineRepository medicineRepository;
    private CustomLogger logger;
    private QMedicine medicine=QMedicine.medicine;
    public MedicineDTO get(long id)
    {
        return new MedicineDTO(getEntity(id));
    }
    public Medicine getEntity(long id)
    {
        return medicineRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Medicine with " + id + "not found" ));
    }
    public Page<MedicineDTO> getPaged(String keyword,Pageable pageable)
    {
        BooleanExpression byName=medicine.name.contains(keyword);
        Page<Medicine> examinations=medicineRepository.findAll( byName,pageable);
        List<MedicineDTO> response= examinations.stream().map(MedicineDTO::new).toList();
        return new PageImpl<MedicineDTO>(response,examinations.getPageable(),examinations.getTotalElements());
    }
}
