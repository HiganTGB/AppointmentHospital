package appointmenthospital.appointmentservice.service;

import appointmenthospital.appointmentservice.client.UserServiceClient;
import appointmenthospital.appointmentservice.model.dto.PatientDTO;
import appointmenthospital.appointmentservice.model.dto.User;
import appointmenthospital.appointmentservice.model.dto.UserDTO;
import appointmenthospital.appointmentservice.model.entity.PatientProfile;
import appointmenthospital.appointmentservice.repository.PatientRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PatientService {
        private UserServiceClient userServiceClient;
        private final PatientRepository patientRepository;
        public PatientDTO create(PatientDTO patientDTO,Principal connectedUser)
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
                        .account_id(userDTO.getId())
                        .patientUUID(null)
                        .build();
                return new PatientDTO(patientRepository.save(patient));
            }catch (FeignException e)
            {
                throw new IllegalStateException("User cannot found");
            }
        };
    public PatientDTO create(String patientUUID,Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try
        {
            UserDTO userDTO= userServiceClient.get(user.getId());
            if(userDTO.getIsStaff())
            {
                throw new IllegalStateException("This is staff");
            }
            PatientProfile patientDTO=patientRepository.getFirstByPatientUUID(patientUUID);
            if(patientRepository.existsByPatientUUIDAndAccount_id(patientUUID,userDTO.getId()))
            {
                throw new IllegalStateException("Exist");
            }
            var profile= PatientProfile.builder()
                    .address(patientDTO.getAddress())
                    .phone(patientDTO.getPhone())
                    .email(patientDTO.getEmail())
                    .gender(patientDTO.isGender())
                    .identityCard(patientDTO.getIdentityCard())
                    .fullName(patientDTO.getFullName())
                    .account_id(userDTO.getId())
                    .patientUUID(patientUUID)
                    .build();
            return new PatientDTO(patientRepository.save(profile));
        }catch (FeignException e)
        {
            throw new IllegalStateException("User cannot found");
        }
    };
    public PatientDTO updateByCustomer(PatientDTO patientDTO,Long id)
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
    public PatientDTO update(PatientDTO patientDTO,Long id)
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
    public PatientDTO updateUUID(String UUID,Long id)
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
}
