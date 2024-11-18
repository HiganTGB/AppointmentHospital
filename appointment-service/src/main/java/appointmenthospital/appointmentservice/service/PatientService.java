package appointmenthospital.appointmentservice.service;

import appointmenthospital.appointmentservice.client.AuthServiceClient;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        @Autowired
        private AuthServiceClient authServiceClient;
        private final PatientRepository patientRepository;
        private final Patient_accountRepository patientAccountRepository;
        private final QPatientProfile patientProfile= QPatientProfile.patientProfile;
        public PatientDTO create(PatientDTO patientDTO, Principal connectedUser)
        {
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            try
            {
                UserDTO userDTO= authServiceClient.get(user.getId());
                if(userDTO.getIsStaff())
                {
                    throw new IllegalStateException("This is staff");
                }
                var patient= PatientProfile.builder()
                        .address(patientDTO.getAddress())
                        .phone(patientDTO.getPhone())
                        .email(patientDTO.getEmail())
                        .gender(patientDTO.getGender())
                        .identityCard(patientDTO.getIdentityCard())
                        .fullName(patientDTO.getFullName())
                        .build();
                PatientProfile profile=patientRepository.save(patient);
                patientAccountRepository.save(new PatientProfile_Account(userDTO.getId(), profile));
                return new PatientDTO(profile);
            }catch (FeignException e)
            {
                throw new IllegalStateException("User cannot found");
            }
        };
    public PatientDTO create(String id, Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try
        {
            UserDTO userDTO= authServiceClient.get(user.getId());
            if(userDTO.getIsStaff())
            {
                throw new IllegalStateException("This is staff");
            }
            PatientProfile profile=patientRepository.getReferenceById(id);
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
    public PatientDTO updateByCustomer(PatientDTO patientDTO, String id)
    {
            PatientProfile profile=patientRepository.getReferenceById(id);
            if(!profile.getExaminations().isEmpty())
            {
                throw new IllegalStateException("Cannot edit");
            }
            profile.setFullName(patientDTO.getFullName());
            profile.setGender(patientDTO.getGender());
            profile.setEmail(patientDTO.getEmail());
            profile.setPhone(patientDTO.getPhone());
            profile.setAddress(profile.getAddress());
            profile.setIdentityCard(profile.getIdentityCard());
            return new PatientDTO(patientRepository.save(profile));
    };
    public PatientDTO update(PatientDTO patientDTO, String id)
    {
        PatientProfile profile=patientRepository.getReferenceById(id);
            profile.setFullName(patientDTO.getFullName());
            profile.setGender(patientDTO.getGender());
            profile.setEmail(patientDTO.getEmail());
            profile.setPhone(patientDTO.getPhone());
            profile.setAddress(profile.getAddress());
            profile.setIdentityCard(profile.getIdentityCard());
            return new PatientDTO(patientRepository.save(profile));
    }
    // Hàm này để cập nhật uuid đã khám trước đó , thay thế vào cái mới
    public PatientProfile updateUUID(String id_old, String id_new)
    {
            PatientProfile profileNoUUID=patientRepository.getReferenceById(id_old);
            PatientProfile profileUUID= patientRepository.getReferenceById(id_new);
            if(!profileNoUUID.getExaminations().isEmpty())
            {
                throw new IllegalStateException("Profile cannot update uuid");
            }
            PatientProfile_Account patientProfileAccount=patientAccountRepository.findFirstByPatientId(profileNoUUID.getId());
            try {
                //Lưu uuid cũ
                patientProfileAccount.setPatient(profileUUID);
                patientAccountRepository.save(patientProfileAccount);
            }catch (DataIntegrityViolationException e)// Đề phòng lỗi đã tồn tại
            {
                // Vẫn xoá như bt
                patientAccountRepository.deleteById(patientProfileAccount.getId());
                return profileUUID;
            }
            // Xoá luôn cái mới
            patientAccountRepository.deleteById(patientProfileAccount.getId());
            return profileUUID;
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
    public PatientDTO get(String id)
    {
        return new PatientDTO(patientRepository.getReferenceById(id));
    }
}
