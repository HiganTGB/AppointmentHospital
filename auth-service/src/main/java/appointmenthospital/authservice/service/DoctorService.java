package appointmenthospital.authservice.service;

import appointmenthospital.authservice.client.InfoServiceClient;
import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.*;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Doctor_Specialty;
import appointmenthospital.authservice.model.entity.QDoctor;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.DoctorRepository;
import appointmenthospital.authservice.repository.Doctor_SpecialtyRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Doctor_SpecialtyRepository doctorSpecialtyRepository;
    private  InfoServiceClient infoServiceClient;
    private final QDoctor doctor=QDoctor.doctor;
    private final Type pageType = new TypeToken<Page<UserDTO>>(){}.getType();
    public DoctorDTO create(DoctorRequest doctorRequest)
    {
        UserDTO userDTO=userService.create(doctorRequest.getUserRequest());
        User user = modelMapper.map(userDTO,User.class);
        var doctor=Doctor.builder()
                .user(user)
                .degree(doctorRequest.getDegree())
                .gender(doctorRequest.isGender())
                .urlAvatar("url-example")
                .build();

        Doctor doctorEntity=doctorRepository.save(doctor);
        for(Long id : doctorRequest.getMedicalSpecialtiesId())
        {
            try
            {
                infoServiceClient.add(id,modelMapper.map(doctor,DoctorDTO.class));
                Doctor_Specialty doctorSpecialty=new Doctor_Specialty(doctor,id);
                doctorSpecialtyRepository.save(doctorSpecialty);
            }catch (Exception e)
            {
                throw new AppException("Specialty with "+ id +" Not Found");
            }
        }
        return new DoctorDTO(doctorEntity);
    }
    public DoctorDTO update(DoctorRequest doctorRequest,Long id)
    {
        Doctor doctorEntity = getEntity(id);
        UserDTO userDTO=userService.update(doctorRequest.getUserRequest(),doctorEntity.getUser().getId());
        User user = modelMapper.map(userDTO,User.class);
        doctorEntity.setUser(user);
        doctorEntity.setDegree(doctorRequest.getDegree());
        doctorEntity.setGender(doctorRequest.isGender());
        Doctor doctor=doctorRepository.save(doctorEntity);

        for(Doctor_Specialty ds: doctor.getDoctorSpecialties())
        {
            try
            {
                infoServiceClient.remove(ds.getId());
            }catch (Exception e)
            {
                throw new AppException("Specialty Doctor with "+ ds.getId() +" Not Found");
            }
        }
        for(Long idSecialty : doctorRequest.getMedicalSpecialtiesId())
        {
            try
            {
                infoServiceClient.add(idSecialty,modelMapper.map(doctor,DoctorDTO.class));
                Doctor_Specialty doctorSpecialty=new Doctor_Specialty(doctor,idSecialty);
                doctorSpecialtyRepository.save(doctorSpecialty);
            }catch (Exception e)
            {
                throw new AppException("Specialty with "+ idSecialty +" Not Found");
            }
        }
        return new DoctorDTO(doctor);
    }
    private Doctor getEntity(Long id)
    {
        return doctorRepository.getReferenceById(id);
    }
    public DoctorDTO get(Long id)
    {
        Doctor doctor=getEntity(id);
        return new DoctorDTO(doctor);
    }
    public Page<DoctorDTO> getPage(String keyword, Pageable pageable,DoctorFilterRequest filterRequest)
    {
        BooleanExpression keywordFilter= doctor.user.firstName.contains(keyword).or(doctor.user.lastName.contains(keyword));
        BooleanExpression emailFilter=doctor.user.email.like(keyword);
        BooleanExpression phoneFilter=doctor.user.phone.like(keyword);
        BooleanExpression specialtyFilter=null;
        BooleanExpression genderFilter=null;
        BooleanExpression degreeFilter=null;
        if(filterRequest!=null)
        {
            if (filterRequest.getGender() != null) {
                genderFilter = switch (filterRequest.getGender()) {
                    case "Female", "FEMALE" -> doctor.gender.isFalse();
                    case "Male", "MALE" -> doctor.gender.isTrue();
                    default -> null;
                };
            }
            specialtyFilter=(!filterRequest.getMedicalSpecialtyIdList().isEmpty()) ?
                    doctor.doctorSpecialties.any().SpecialtyId.in(filterRequest.getMedicalSpecialtyIdList()):null;
            degreeFilter=(!filterRequest.getDegree().isEmpty())? doctor.degree.in(filterRequest.getDegree()):null;
        }
        Predicate predicate=keywordFilter.or(emailFilter).or(phoneFilter).and(genderFilter).and(degreeFilter).and(specialtyFilter);
        Page<Doctor> pageEntity=doctorRepository.findAll(predicate,pageable);
        return  modelMapper.map(pageEntity,pageType);
    }

}
