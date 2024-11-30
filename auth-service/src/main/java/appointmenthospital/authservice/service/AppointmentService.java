package appointmenthospital.authservice.service;

import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.entity.Appointment;
import appointmenthospital.authservice.model.entity.QAppointment;
import appointmenthospital.authservice.repository.AppointmentRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private CustomLogger logger;
    private QAppointment appointment=QAppointment.appointment;

    public Page<AppointmentDTO> getPaged(String keyword, Pageable pageable,Long doctor_id,Long profile_id,Long patient_id)
    {
        BooleanExpression byDoctor= (doctor_id!=null) ?  appointment.doctor.id.eq(doctor_id):null;
        BooleanExpression byProfile= (profile_id!=null) ? appointment.profile.id.eq(doctor_id):null;
        BooleanExpression byPatient= (patient_id!=null) ? appointment.profile.patient.id.eq(doctor_id):null;

        Page<Appointment> appointments=appointmentRepository.findAll(byDoctor.or(byPatient).or(byProfile),pageable);
        List<AppointmentDTO> response = appointments.stream().map(AppointmentDTO::new).toList();
        return new PageImpl<AppointmentDTO>(response,appointments.getPageable(),appointments.getTotalElements());
    }
    public Appointment getEntity(long id)
    {
        return appointmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not found"));
    }
    public AppointmentDTO get(long id)
    {
        return new AppointmentDTO(getEntity(id));
    }

}
