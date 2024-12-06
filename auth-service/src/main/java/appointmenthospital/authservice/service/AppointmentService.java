package appointmenthospital.authservice.service;

import appointmenthospital.authservice.email.Mail;
import appointmenthospital.authservice.email.MailService;
import appointmenthospital.authservice.log.CustomLogger;
import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.dto.ProfileDTO;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.payment.PaymentDTO;
import appointmenthospital.authservice.payment.PaymentService;
import appointmenthospital.authservice.repository.AppointmentRepository;
import appointmenthospital.authservice.repository.DoctorRepository;
import appointmenthospital.authservice.repository.ProfileRepository;
import appointmenthospital.authservice.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final ProfileRepository profileRepository;
    private final AppointmentRepository appointmentRepository;
    private final QAppointment appointment = QAppointment.appointment;
    private final UserService userService;
    private final PatientService patientService;
    private CustomLogger logger;
    private PaymentService paymentService;
    private MailService mailService;

    @Value("${app.price}")
    public BigDecimal price;

    public AppointmentService(DoctorRepository doctorRepository, ProfileRepository profileRepository, AppointmentRepository appointmentRepository, UserService userService, CustomLogger logger, PaymentService paymentService, MailService mailService, PatientService patientService) {
        this.doctorRepository = doctorRepository;
        this.profileRepository = profileRepository;
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.logger = logger;
        this.paymentService = paymentService;
        this.mailService = mailService;
        this.patientService = patientService;
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

    public Page<AppointmentDTO> getPaged(String keyword, Pageable pageable, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        BooleanExpression byPatient = appointment.profile.patient.id.eq(user.getPatient().getId());
        BooleanExpression inFuture = appointment.atTime.gt(LocalDateTime.now());
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

    public AppointmentDTO create(AppointmentDTO dto, HttpServletRequest request) {
        Appointment profile = Appointment.builder()
                .doctor(doctorRepository.findById(dto.getDoctorId()).orElseThrow())
                .profile(profileRepository.findById(dto.getProfile()).get())
                .number((int) dto.getNumber())
                .atTime(dto.getAtTime())
                .state((int) dto.getState())
                .price(price)
                .build();
        profile = appointmentRepository.save(profile);
        PaymentDTO.VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, profile.getPrice(), String.valueOf(profile.getId()));
        AppointmentDTO result = new AppointmentDTO(profile);
        result.setPayment(vnPayResponse.paymentUrl);
        return result;
    }

    public Boolean pay(long id) {
        Appointment profile = getEntity(id);
        profile.setState(2);
        profile = appointmentRepository.save(profile);
        AppointmentDTO result = new AppointmentDTO(profile);
        mail(profile);
        return true;
    }


    public Boolean mail(Appointment appointment) {
        try {
            String to = appointment.getProfile().getPatient().getUser().getEmail();
            String subject = "Xác nhận đặt lịch khám bệnh";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String content = String.format("Kính gửi quý khách,\n" +
                    "\n" +
                    "Chúng tôi xin xác nhận lịch khám bệnh của quý khách đã được đặt thành công.\n" +
                    "\n" +
                    "**Thông tin lịch khám:**\n" +
                    "  - Mã khám: %s \n" +
                    "  - Ngày khám: %s \n" +
                    "  - Giờ khám: %s\n" +
                    "  - Bác sĩ: %s\n" +
                    "\n" +
                    "Vui lòng mang theo giấy tờ tùy thân và kết quả xét nghiệm (nếu có) khi đến khám.\n" +
                    "\n" +
                    "Trân trọng,\n" +
                    "Phòng khám", appointment.getId(), appointment.getAtTime().format(formatter), appointment.getAtTime().format(formatterTime), appointment.getDoctor().getUser().getFullName());
            Mail mail = new Mail();
            mail.setMailTo(to);
            mail.setMailSubject(subject);
            mail.setMailContent(content);
            mailService.sendEmail(mail);
            return true;
        } catch (Exception e) {
            return false;
        }
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
