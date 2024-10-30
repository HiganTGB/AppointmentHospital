package appointmenthospital.infoservice.service;

import appointmenthospital.infoservice.exc.ItemNotFoundException;
import appointmenthospital.infoservice.model.dto.DoctorDTO;
import appointmenthospital.infoservice.model.dto.MedicalSpecialtyDTO;
import appointmenthospital.infoservice.model.entity.MedicalSpecialty;
import appointmenthospital.infoservice.model.entity.Specialty_Doctor;
import appointmenthospital.infoservice.model.entity.QMedicalSpecialty;
import appointmenthospital.infoservice.repository.MedicalSpecialtyRepository;
import appointmenthospital.infoservice.repository.Specialty_DoctorRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
@RequiredArgsConstructor
public class MedicalSpecialtyService {
    private final Type pageType = new TypeToken<Page<MedicalSpecialtyDTO>>(){}.getType();
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final Specialty_DoctorRepository specialtyDoctorRepository;
    private final ModelMapper modelMapper;
    private final QMedicalSpecialty medicalSpecialty= QMedicalSpecialty.medicalSpecialty;
    public MedicalSpecialtyDTO create(MedicalSpecialtyDTO medicalSpecialtyDTO)
    {
        var medical=MedicalSpecialty.builder().name(medicalSpecialtyDTO.getName()).description(medicalSpecialtyDTO.getDescription()).build();
        return modelMapper.map(medicalSpecialtyRepository.save(medical),MedicalSpecialtyDTO.class);
    }
    public MedicalSpecialtyDTO update(MedicalSpecialtyDTO medicalSpecialtyDTO,Long id)
    {
        MedicalSpecialty medicalSpecialty=medicalSpecialtyRepository.getReferenceById(id);
        medicalSpecialty.setName(medicalSpecialtyDTO.getName());
        medicalSpecialty.setDescription(medicalSpecialtyDTO.getDescription());
        return modelMapper.map(medicalSpecialtyRepository.save(medicalSpecialty),MedicalSpecialtyDTO.class);
    }
    public Page<MedicalSpecialtyDTO> getPage(String keyword, Pageable pageable)
    {
        BooleanExpression expression= medicalSpecialty.name.contains(keyword);
        return modelMapper.map(medicalSpecialtyRepository.findAll(expression,pageable),pageType);
    }
    public MedicalSpecialtyDTO get(Long id)
    {
        return modelMapper.map(getEntity(id),MedicalSpecialtyDTO.class);
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


    public Boolean addDoctor(DoctorDTO doctorDTO, Long id)
    {
        MedicalSpecialty medicalSpecialty=getEntity(id);
        Specialty_Doctor medicalSpecialtyDoctor=new Specialty_Doctor(medicalSpecialty,doctorDTO.getId());
        specialtyDoctorRepository.save(medicalSpecialtyDoctor);
        return true;
    }
    public Boolean removeDoctor(Long id)
    {
        Specialty_Doctor medicalSpecialtyDoctor= specialtyDoctorRepository.getReferenceById(id);
        specialtyDoctorRepository.delete(medicalSpecialtyDoctor);
        return true;
    }
    @Deprecated
    public Boolean changeDoctor(Long id, Long idNew)
    {
        MedicalSpecialty medicalSpecialty=getEntity(idNew);
        Specialty_Doctor specialtyDoctor=specialtyDoctorRepository.getReferenceById(id);
        specialtyDoctor.setMedicalSpecialty(medicalSpecialty);
        specialtyDoctorRepository.save(specialtyDoctor);
        return true;
    }

}