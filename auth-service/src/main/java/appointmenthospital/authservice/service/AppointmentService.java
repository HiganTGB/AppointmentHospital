package appointmenthospital.authservice.service;

import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.dto.ProfileDTO;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.repository.AppointmentRepository;
import appointmenthospital.authservice.repository.DoctorRepository;
import appointmenthospital.authservice.repository.ProfileRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final ProfileRepository profileRepository;
    private final AppointmentRepository appointmentRepository;
    private CustomLogger logger;
    private final QAppointment appointment = QAppointment.appointment;

    public AppointmentService(DoctorRepository doctorRepository, ProfileRepository profileRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.profileRepository = profileRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Page<AppointmentDTO> getPaged(String keyword, Pageable pageable, Long doctor_id, Long profile_id, Long patient_id) {
        BooleanExpression predicate = null;
        if (doctor_id == null) {
            BooleanExpression eq = appointment.doctor.id.eq(doctor_id);
            if (predicate == null) predicate = eq;
            else predicate.or(eq);
        }
        if (profile_id == null) {
            BooleanExpression eq = appointment.profile.id.eq(profile_id);
            if (predicate == null) predicate = eq;
            else predicate.or(eq);
        }
        if (patient_id == null) {
            BooleanExpression eq = appointment.profile.patient.id.eq(patient_id);
            if (predicate == null) predicate = eq;
            else predicate.or(eq);
        }
        Page<Appointment> appointments = predicate == null ? appointmentRepository.findAll(pageable)
                : appointmentRepository.findAll(predicate, pageable);
        List<AppointmentDTO> response = appointments.stream().map(AppointmentDTO::new).toList();
        return new PageImpl<>(response, appointments.getPageable(), appointments.getTotalElements());
    }

    public Page<AppointmentDTO> getPaged(String keyword, Pageable pageable, Long patient_id) {
        BooleanExpression byPatient = QAppointment.appointment.profile.patient.user.id.eq(patient_id);
        BooleanExpression inFuture = QAppointment.appointment.atTime.gt(LocalDateTime.now());
        Page<Appointment> appointments = appointmentRepository.findAll(byPatient.and(inFuture), pageable);
        List<AppointmentDTO> response = appointments.stream().map(AppointmentDTO::new).toList();
        return new PageImpl<>(response, appointments.getPageable(), appointments.getTotalElements());
    }

    public Appointment getEntity(long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    public AppointmentDTO get(long id) {
        return new AppointmentDTO(getEntity(id));
    }

    public AppointmentDTO create(AppointmentDTO dto) {
        Appointment profile = Appointment.builder()
                .doctor(doctorRepository.findById(dto.getDoctorId()).orElseThrow())
                .profile(profileRepository.findById(dto.getProfile()).get())
                .number((int) dto.getNumber())
                .atTime(dto.getAtTime())
                .state((int) dto.getState())
                .build();
        return new AppointmentDTO(appointmentRepository.save(profile));
    }
}
