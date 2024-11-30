package appointmenthospital.authservice.service;

import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.QDoctor;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.DoctorRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private DoctorRepository doctorRepository;
    private UserService userService;
    private CustomLogger customLogger;
    private final QDoctor doctor = QDoctor.doctor;

    public Page<DoctorDTO> getPaged(String keyword, Pageable pageable)
    {
        BooleanExpression byName= doctor.user.fullName.contains(keyword);
        Page<Doctor> doctors=doctorRepository.findAll(byName,pageable);
        List<DoctorDTO> responses = doctors.getContent().stream().map(DoctorDTO::new).toList();
        return new PageImpl<DoctorDTO>(responses,doctors.getPageable(),doctors.getTotalElements());
    }
    private Doctor getEntity(long id)
    {
        return doctorRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Patient with " +id + "not found" ));
    }
    public DoctorDTO get(long id)
    {
        return new DoctorDTO(getEntity(id));
    }

    public DoctorDTO create(DoctorDTO dto)
    {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(dto.getEmail());
        userDTO.setPhone(dto.getPhone());
        userDTO.setFullName(dto.getFullName());
        userDTO= userService.create(userDTO,false);
        var doctor=Doctor.builder()
                .certificate(dto.getCertificate())
                .position(dto.getPosition())
                .user(userService.getEntity(userDTO.getId()))
                .gender(dto.getGender())
                .build();
        return new DoctorDTO(doctorRepository.save(doctor));
    }
    public DoctorDTO update(DoctorDTO dto,long id)
    {
        Doctor doctor=getEntity(id);
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(dto.getEmail());
        userDTO.setPhone(dto.getPhone());
        userDTO.setFullName(dto.getFullName());
        userDTO= userService.update(userDTO,false,doctor.getUser().getId());
        doctor.setUser(userService.getEntity(userDTO.getId()));
        doctor.setCertificate(dto.getCertificate());
        doctor.setPosition(dto.getPosition());
        doctor.setGender(dto.getGender());
        return new DoctorDTO(doctorRepository.save(doctor));
    }
    public Boolean delete(long id)
    {
        Doctor doctor=getEntity(id);;
        return userService.lock(doctor.getUser().getId());
    }
    public DoctorDTO getByUser(Principal  connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var doctor=doctorRepository.findByUserId(user.getId()).orElseThrow(()->new EntityNotFoundException("Not found"));
        return new DoctorDTO(doctor);
    }


}
