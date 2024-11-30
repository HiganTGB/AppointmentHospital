package appointmenthospital.authservice.service;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.ExaminationDTO;
import appointmenthospital.authservice.model.entity.Appointment;
import appointmenthospital.authservice.model.entity.Examination;
import appointmenthospital.authservice.model.entity.QExamination;
import appointmenthospital.authservice.repository.AppointmentRepository;
import appointmenthospital.authservice.repository.ExaminationRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExaminationService {
    private ExaminationRepository examinationRepository;
    private AppointmentRepository appointmentRepository;
    private QExamination examination=QExamination.examination;
    public Page<ExaminationDTO> getPaged(Pageable pageable,long doctor_id)
    {
        BooleanExpression byDoctor=(doctor_id!=-1) ? examination.examinationDetails.any().doctor.id.eq(doctor_id): null;
        Page<Examination> examinations=examinationRepository.findAll(byDoctor,pageable);
        List<ExaminationDTO> response= examinations.stream().map(ExaminationDTO::new).toList();
        return new PageImpl<ExaminationDTO>(response,examinations.getPageable(),examinations.getTotalElements());
    }
    public Examination getEntity(long id)
    {
        return examinationRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not found"));
    }
    public ExaminationDTO get(long id)
    {
        return new ExaminationDTO(getEntity(id));
    }
    private Appointment getAppointment(long id)
    {
        return appointmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not found"));
    }
    public ExaminationDTO create(ExaminationDTO dto)
    {
        Appointment appointment=getAppointment(dto.getAppointmentId());
        var examination= Examination.builder()
                .appointment(appointment)
                .description(dto.getDescription())
                .state(dto.getState())
                .diagnostic(dto.getDiagnostic())
                .build();
        return new ExaminationDTO(examinationRepository.save(examination));
    }
    public ExaminationDTO update(ExaminationDTO dto,long id)
    {
        Examination examination=getEntity(id);
        Appointment appointment=getAppointment(dto.getAppointmentId());
        examination.setAppointment(appointment);
        examination.setDescription(dto.getDescription());
        examination.setState(dto.getState());
        examination.setDiagnostic(dto.getDiagnostic());
        return new ExaminationDTO(examinationRepository.save(examination));
    }
    public Boolean delete(long id)
    {
        Examination examination=getEntity(id);
        try
        {
            examinationRepository.delete(examination);
            return true;
        }catch (DataIntegrityViolationException e)
        {
            throw new AppException("Cannot delete");
        }
    }
}
