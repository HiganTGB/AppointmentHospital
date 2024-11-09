package appointmenthospital.infoservice.service;

import appointmenthospital.infoservice.exc.ItemNotFoundException;
import appointmenthospital.infoservice.model.dto.DoctorDomain;
import appointmenthospital.infoservice.model.dto.MedicalSpecialtyDTO;
import appointmenthospital.infoservice.model.entity.MedicalSpecialty;
import appointmenthospital.infoservice.model.entity.Specialty_Doctor;
import appointmenthospital.infoservice.model.entity.QMedicalSpecialty;
import appointmenthospital.infoservice.repository.MedicalSpecialtyRepository;
import appointmenthospital.infoservice.repository.Specialty_DoctorRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalSpecialtyService {

    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final Specialty_DoctorRepository specialtyDoctorRepository;
    private final QMedicalSpecialty medicalSpecialty= QMedicalSpecialty.medicalSpecialty;
    public MedicalSpecialtyDTO create(MedicalSpecialtyDTO medicalSpecialtyDTO)
    {
        var medical=MedicalSpecialty.builder().name(medicalSpecialtyDTO.getName()).description(medicalSpecialtyDTO.getDescription()).price(medicalSpecialtyDTO.getPrice()).build();
        return new MedicalSpecialtyDTO(medicalSpecialtyRepository.save(medical));
    }
    public MedicalSpecialtyDTO update(MedicalSpecialtyDTO medicalSpecialtyDTO,Long id)
    {
        MedicalSpecialty medicalSpecialty=medicalSpecialtyRepository.getReferenceById(id);
        medicalSpecialty.setName(medicalSpecialtyDTO.getName());
        medicalSpecialty.setDescription(medicalSpecialtyDTO.getDescription());
        medicalSpecialty.setPrice(medicalSpecialtyDTO.getPrice());
        return new MedicalSpecialtyDTO(medicalSpecialtyRepository.save(medicalSpecialty));
    }
    public Page<MedicalSpecialtyDTO> getPage(String keyword, Pageable pageable)
    {
        BooleanExpression expression= medicalSpecialty.name.contains(keyword);
        return (medicalSpecialtyRepository.findAll(expression,pageable).map(MedicalSpecialtyDTO::new));
    }
    public MedicalSpecialtyDTO get(Long id)
    {
        return new MedicalSpecialtyDTO(getEntity(id));
    }
    public List<MedicalSpecialtyDTO> getAll()
    {
        return medicalSpecialtyRepository.findAll().stream().map(MedicalSpecialtyDTO::new).toList();
    }
    private MedicalSpecialty getEntity(Long id)
    {
        try
        {
            return medicalSpecialtyRepository.getReferenceById(id);
        }catch (EmptyResultDataAccessException e)
        {
            throw new ItemNotFoundException("Medical Specialty with " +id + " not Found");
        }
    }
    public Long addDoctor(DoctorDomain doctorDomain, Long id)
    {
        MedicalSpecialty medicalSpecialty=getEntity(id);
        Specialty_Doctor medicalSpecialtyDoctor=new Specialty_Doctor(medicalSpecialty, doctorDomain.getId());
        try{
            medicalSpecialtyDoctor=specialtyDoctorRepository.save(medicalSpecialtyDoctor);
            return  medicalSpecialtyDoctor.getId();
        }catch (DataIntegrityViolationException ex)
        {
            return -1L;
        }
    }
    public Boolean removeDoctor(Long id)
    {
        Specialty_Doctor medicalSpecialtyDoctor= specialtyDoctorRepository.getReferenceById(id);
        specialtyDoctorRepository.delete(medicalSpecialtyDoctor);
        return true;
    }
    public Boolean changeDoctor(Long id, Long idNew)
    {
        MedicalSpecialty medicalSpecialty=getEntity(idNew);
        Specialty_Doctor specialtyDoctor=specialtyDoctorRepository.getReferenceById(id);
        specialtyDoctor.setMedicalSpecialty(medicalSpecialty);
        specialtyDoctorRepository.save(specialtyDoctor);
        return true;
    }
    public List<MedicalSpecialtyDTO> getAllByDoctorID(Long id)
    {
       List<Specialty_Doctor> specialtyDoctors=specialtyDoctorRepository.getAllByDoctorID(id);
       return specialtyDoctors.stream()
               .map(x->new MedicalSpecialtyDTO(
                       x.getMedicalSpecialty()
               )).
               toList();
    }

}