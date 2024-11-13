package appointmenthospital.authservice.service;

import appointmenthospital.authservice.client.FileStorageClient;
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
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Doctor_SpecialtyRepository doctorSpecialtyRepository;
    @Autowired
    private FileStorageClient fileStorageClient;
    @Autowired
    private  InfoServiceClient infoServiceClient;
    private final QDoctor doctor=QDoctor.doctor;
    private final Type pageType = new TypeToken<Page<UserDTO>>(){}.getType();
    @Transactional
    public DoctorDTO create(DoctorRequest doctorRequest,MultipartFile file)
    {
        ResponseEntity<String> response=fileStorageClient.uploadImageToFileSystem(file);
        String imageURL= response.getBody();
        UserDTO userDTO=userService.create(doctorRequest.getUserRequest());
        User user = modelMapper.map(userDTO,User.class);
        var doctor=Doctor.builder()
                .user(user)
                .degree(doctorRequest.getDegree())
                .gender(doctorRequest.getGender())
                .urlAvatar(imageURL)
                .build();

        Doctor doctorEntity=doctorRepository.save(doctor);
        for(Long id : doctorRequest.getMedicalSpecialtiesId())
        {
      //      try
   //         {
                Long id_spec= infoServiceClient.add(id,new DoctorDomain(doctor));
                Doctor_Specialty doctorSpecialty=new Doctor_Specialty(doctor,id,id_spec);
                doctorSpecialtyRepository.save(doctorSpecialty);

      //      }catch (Exception e)
     //       {
             //   throw new AppException("Specialty with "+ id +" Not Found");
    //        }
        }
        doctorEntity =getEntity(doctorEntity.getId());
        return new DoctorDTO(doctorEntity);
    }
    public List<DoctorListDTO> getList(String keyword,DoctorFilterRequest filterRequest)
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
                genderFilter=doctor.gender.in(filterRequest.getGender());
            }
            specialtyFilter=(!filterRequest.getMedicalSpecialtyIdList().isEmpty()) ?
                    doctor.doctorSpecialties.any().SpecialtyId.in(filterRequest.getMedicalSpecialtyIdList()):null;
            degreeFilter=(!filterRequest.getDegree().isEmpty())? doctor.degree.in(filterRequest.getDegree()):null;
        }
        Predicate predicate=keywordFilter.or(emailFilter).or(phoneFilter).and(genderFilter).and(degreeFilter).and(specialtyFilter);
        List<DoctorListDTO> dtos=new ArrayList<>();
        doctorRepository.findAll(predicate).forEach(x->dtos.add(new DoctorListDTO(x)));
        return dtos;
    }
    public DoctorDTO update(DoctorRequest doctorRequest,MultipartFile file,Long id)
    {
        Doctor doctorEntity = getEntity(id);
        UserDTO userDTO=userService.update(doctorRequest.getUserRequest(),doctorEntity.getUser().getId());
        User user = modelMapper.map(userDTO,User.class);
        doctorEntity.setUser(user);
        doctorEntity.setDegree(doctorRequest.getDegree());
        doctorEntity.setGender(doctorRequest.getGender());
        try
        {
            fileStorageClient.deleteImageFromFileSystem(doctorEntity.getUrlAvatar());
        }catch (FeignException e)
        {
            throw new AppException("Cannot delete image");
        }
        ResponseEntity<String> response=fileStorageClient.uploadImageToFileSystem(file);
        String imageURL= response.getBody();
        doctorEntity.setUrlAvatar(imageURL);
        Doctor doctor=doctorRepository.save(doctorEntity);

        for(Doctor_Specialty ds: doctor.getDoctorSpecialties())
        {
            try
            {
                Boolean response_ds =infoServiceClient.remove(ds.getId());
                doctorSpecialtyRepository.deleteById(ds.getId());
            }catch (FeignException e)
            {
                throw new AppException("Cannot remove doctor specialty");
            }
        }
        for(Long idSecialty : doctorRequest.getMedicalSpecialtiesId())
        {
            try
            {
                Long id_spec= infoServiceClient.add(idSecialty,new DoctorDomain(doctor));
                Doctor_Specialty doctorSpecialty=new Doctor_Specialty(doctor,idSecialty,id_spec);
                doctorSpecialtyRepository.save(doctorSpecialty);
            }catch (FeignException e)
            {
                throw new AppException("Cannot add doctor specialty");
            }
        }
        return new DoctorDTO(doctor);
    }
    public Doctor getEntity(Long id)
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
                genderFilter=doctor.gender.in(filterRequest.getGender());
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
