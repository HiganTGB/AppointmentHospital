package appointmenthospital.authservice.service;

import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.PatientDTO;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.entity.Patient;
import appointmenthospital.authservice.model.entity.QPatient;
import appointmenthospital.authservice.model.entity.User;
import appointmenthospital.authservice.repository.PatientRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private UserService userService;
    private CustomLogger customLogger;
    private final QPatient patient= QPatient.patient;
    @Autowired
    public PatientService(PatientRepository patientRepository, UserService userService, PasswordEncoder passwordEncoder, CustomLogger customLogger) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.customLogger = customLogger;
    }
    public Page<PatientDTO> getPaged(String keyword, Pageable pageable)
    {
        BooleanExpression byName=patient.user.fullName.contains(keyword);
        Page<Patient> patients=patientRepository.findAll(byName,pageable);
        List<PatientDTO> responses = patients.getContent().stream().map(PatientDTO::new).toList();
        return new PageImpl<PatientDTO>(responses,patients.getPageable(),patients.getTotalElements());
    }
    public Patient getEntity(long id)
    {
        return patientRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Patient with " +id + "not found" ));
    }
    public PatientDTO get(long id)
    {
        return new PatientDTO(getEntity(id));
    }
    public Boolean delete(long id)
    {
        Patient patient=getEntity(id);
        return userService.lock(patient.getUser().getId());
    }
    public PatientDTO create(PatientDTO dto)
    {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(dto.getEmail());
        userDTO.setPhone(dto.getPhone());
        userDTO.setFullName(dto.getFullName());
        userDTO= userService.create(userDTO,false);
        var patient=Patient.builder()
                .user(userService.getEntity(userDTO.getId()))
                .build();
        return new PatientDTO(patientRepository.save(patient));
    }
    public PatientDTO update(PatientDTO dto,long id)
    {
        Patient patient=getEntity(id);
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(dto.getEmail());
        userDTO.setPhone(dto.getPhone());
        userDTO.setFullName(dto.getFullName());
        userDTO= userService.update(userDTO,false,patient.getUser().getId());
        patient.setUser(userService.getEntity(userDTO.getId()));
        return new PatientDTO(patientRepository.save(patient));
    }



}
