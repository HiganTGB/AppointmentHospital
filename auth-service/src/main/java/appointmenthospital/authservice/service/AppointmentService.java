package appointmenthospital.authservice.service;

import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.entity.Appointment;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.QAppointment;
import appointmenthospital.authservice.payment.PaymentDTO;
import appointmenthospital.authservice.payment.PaymentService;
import appointmenthospital.authservice.repository.AppointmentRepository;
import appointmenthospital.authservice.repository.DoctorRepository;
import appointmenthospital.authservice.repository.ProfileRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final ProfileRepository profileRepository;
    private final AppointmentRepository appointmentRepository;
    private CustomLogger logger;
    private PaymentService paymentService;
    private final QAppointment appointment = QAppointment.appointment;
    @Value("${app.price}")
    public BigDecimal price;

    public AppointmentService(DoctorRepository doctorRepository, ProfileRepository profileRepository, AppointmentRepository appointmentRepository, PaymentService paymentService) {
        this.doctorRepository = doctorRepository;
        this.profileRepository = profileRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentService = paymentService;
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

    public Appointment getEntity(long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    public AppointmentDTO get(long id) {
        return new AppointmentDTO(getEntity(id));
    }

    public AppointmentDTO create(AppointmentDTO dto, HttpServletRequest request) {
        Appointment profile = Appointment.builder()
                .doctor(doctorRepository.findById(dto.getDoctorId()).orElseThrow())
                .profile(profileRepository.findById(dto.getProfile()).get())
                .number((int) dto.getNumber())
                .atTime(dto.getAtTime())
                .state((int) dto.getState())
                .price(price)
                .build();
        profile=appointmentRepository.save(profile);
        PaymentDTO.VNPayResponse vnPayResponse=paymentService.createVnPayPayment(request,profile.getPrice(),String.valueOf(profile.getId()));
        AppointmentDTO result=new AppointmentDTO(profile);
        result.setPayment(vnPayResponse.paymentUrl);
        return result;
    }
    public Boolean pay(long id) {
        Appointment profile = getEntity(id);
        profile.setState(2);
        profile=appointmentRepository.save(profile);
        AppointmentDTO result=new AppointmentDTO(profile);
        return true;
    }
}
