package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.PatientDTO;
import appointmenthospital.authservice.model.dto.ProfileDTO;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.repository.ProfileRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private ProfileRepository profileRepository;
    private PatientService patientService;
    private CustomLogger logger;
    private QProfile profile=QProfile.profile;


    public Page<ProfileDTO> getPaged(String keyword, Pageable pageable)
    {
        BooleanExpression byName= profile.fullName.contains(keyword);
        Page<Profile> profiles=profileRepository.findAll(byName,pageable);
        List<ProfileDTO> responses = profiles.getContent().stream().map(ProfileDTO::new).toList();
        return new PageImpl<ProfileDTO>(responses,profiles.getPageable(),profiles.getTotalElements());
    }
    public Profile getEntity(long id)
    {
        return profileRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Profile not found"));
    }
    public ProfileDTO get(long id)
    {
        return new ProfileDTO(getEntity(id));
    }
    public ProfileDTO create(ProfileDTO dto)
    {
        Patient patient=patientService.getEntity(dto.getPatientId());
        Profile profile=Profile.builder()
                .patient(patient)
                .fullName(dto.getFullName())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .build();
        return new ProfileDTO(profileRepository.save(profile));
    }
    public ProfileDTO update(ProfileDTO dto, long id)
    {
        Profile profile=getEntity(id);
        profile.setFullName(dto.getFullName());
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setGender(dto.getGender());
        return new ProfileDTO(profileRepository.save(profile));
    }
    public Boolean delete(long id)
    {
        Profile profile=getEntity(id);
        try
        {
            profileRepository.delete(profile);
            return true;
        }catch (DataIntegrityViolationException e)
        {
            throw new AppException("Cannot delete");
        }
    }
    public List<ProfileDTO> getByPatient(Principal connectedUser)
    {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        BooleanExpression byPatient=profile.patient.user.id.eq(user.getId());

        List<ProfileDTO> profileDTOS=new ArrayList<>();
        profileRepository.findAll(byPatient).forEach(
                p-> profileDTOS.add(new ProfileDTO(p))
        );
        return profileDTOS;
    }
}
