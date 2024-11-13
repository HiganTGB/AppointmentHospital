package appointmenthospital.appointmentservice.service;

import appointmenthospital.appointmentservice.client.UserServiceClient;
import appointmenthospital.appointmentservice.model.dto.PatientDTO;
import appointmenthospital.appointmentservice.model.dto.User;
import appointmenthospital.appointmentservice.model.dto.UserDTO;
import appointmenthospital.appointmentservice.model.entity.PatientProfile;
import appointmenthospital.appointmentservice.model.entity.PatientProfile_Account;
import appointmenthospital.appointmentservice.model.entity.QPatientProfile;
import appointmenthospital.appointmentservice.repository.PatientRepository;
import appointmenthospital.appointmentservice.repository.Patient_accountRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {
        private UserServiceClient userServiceClient;
        private final PatientRepository patientRepository;
        private final Patient_accountRepository patientAccountRepository;
        private final QPatientProfile patientProfile= QPatientProfile.patientProfile;
        public PatientDTO create(PatientDTO patientDTO, Principal connectedUser)
        {
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            try
            {
                UserDTO userDTO= userServiceClient.get(user.getId());
                if(userDTO.getIsStaff())
                {
                    throw new IllegalStateException("This is staff");
                }
                var patient= PatientProfile.builder()
                        .address(patientDTO.getAddress())
                        .phone(patientDTO.getPhone())
                        .email(patientDTO.getEmail())
                        .gender(patientDTO.isGender())
                        .identityCard(patientDTO.getIdentityCard())
                        .fullName(patientDTO.getFullName())
                        .patientUUID(null)
                        .build();
                PatientProfile profile=patientRepository.save(patient);
                patientAccountRepository.save(new PatientProfile_Account(userDTO.getId(), profile));
                return new PatientDTO(profile);
            }catch (FeignException e)
            {
                throw new IllegalStateException("User cannot found");
            }
        };
    public PatientDTO create(String patientUUID, Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try
        {
            UserDTO userDTO= userServiceClient.get(user.getId());
            if(userDTO.getIsStaff())
            {
                throw new IllegalStateException("This is staff");
            }
            PatientProfile profile=patientRepository.getFirstByPatientUUID(patientUUID);
            if(patientAccountRepository.existsByAccountIDAndPatientId(userDTO.getId(),profile.getId()))
            {
                throw new IllegalStateException("Exist");
            }
            patientAccountRepository.save(new PatientProfile_Account(userDTO.getId(), profile));
            return new PatientDTO(profile);
        }catch (FeignException e)
        {
            throw new IllegalStateException("User cannot found");
        }
    };
    public PatientDTO updateByCustomer(PatientDTO patientDTO, Long id)
    {
        try
        {   PatientProfile profile=patientRepository.getReferenceById(id);
            if(profile.getPatientUUID()==null)
            {
                throw new IllegalStateException("Cannot edit");
            }
            profile.setFullName(patientDTO.getFullName());
            profile.setGender(patientDTO.isGender());
            profile.setEmail(patientDTO.getEmail());
            profile.setPhone(patientDTO.getPhone());
            profile.setAddress(profile.getAddress());
            profile.setIdentityCard(profile.getIdentityCard());
            return new PatientDTO(patientRepository.save(profile));
        }catch (FeignException e)
        {
            throw new IllegalStateException("User cannot found");
        }
    };
    public PatientDTO update(PatientDTO patientDTO, Long id)
    {
        try
        {   PatientProfile profile=patientRepository.getReferenceById(id);
            profile.setFullName(patientDTO.getFullName());
            profile.setGender(patientDTO.isGender());
            profile.setEmail(patientDTO.getEmail());
            profile.setPhone(patientDTO.getPhone());
            profile.setAddress(profile.getAddress());
            profile.setIdentityCard(profile.getIdentityCard());
            profile.setPatientUUID(patientDTO.getPatient_id());
            return new PatientDTO(patientRepository.save(profile));
        }catch (FeignException e)
        {
            throw new IllegalStateException("User cannot found");
        }
    }
    public PatientDTO updateUUID(String UUID, Long id)
    {
        try
        {   PatientProfile profile=patientRepository.getReferenceById(id);
            PatientProfile patientDTO= patientRepository.getFirstByPatientUUID(UUID);
            profile.setFullName(patientDTO.getFullName());
            profile.setGender(patientDTO.isGender());
            profile.setEmail(patientDTO.getEmail());
            profile.setPhone(patientDTO.getPhone());
            profile.setAddress(profile.getAddress());
            profile.setIdentityCard(profile.getIdentityCard());
            profile.setPatientUUID(patientDTO.getPatientUUID());
            return new PatientDTO(patientRepository.save(profile));
        }catch (FeignException e)
        {
            throw new IllegalStateException("User cannot found");
        }
    }
    public List<PatientDTO> getAllByAccountID(Long id)
    {
        List<PatientDTO> patientDTOS=new ArrayList<>();
        BooleanExpression byAccount=patientProfile.patientProfile_accounts.any().accountID.eq(id);
        patientRepository.findAll(byAccount).forEach(x-> patientDTOS.add(new PatientDTO(x)));
        return patientDTOS;
    }
    public Page<PatientDTO> getPage(String keyword, Pageable pageable)
    {
        BooleanExpression byName=patientProfile.fullName.contains(keyword);
        return patientRepository.findAll(byName,pageable).map(PatientDTO::new);
    }
    public PatientDTO get(Long id)
    {
        return new PatientDTO(patientRepository.getReferenceById(id));
    }
}
