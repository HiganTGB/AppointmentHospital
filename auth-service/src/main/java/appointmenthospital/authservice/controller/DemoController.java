package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.*;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    private final RoleService roleService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final ProfileService profileService;
    private final SchedulerService schedulerService;
    private final AppointmentService appointmentService;
    private final PasswordEncoder passwordEncoder;

    public DemoController(RoleService roleService, UserService userService, DoctorService doctorService, PatientService patientService, ProfileService profileService, SchedulerService schedulerService, AppointmentService appointmentService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.profileService = profileService;
        this.schedulerService = schedulerService;
        this.appointmentService = appointmentService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<String> sayHello() {
        RoleDTO admin = new RoleDTO(Role.builder().name("admin").description("admin").permissions(new byte[Role.PERMISSIONS_STRING_LIMIT]).build()),
                doctor = new RoleDTO(Role.builder().name("doctor").description("doctor").permissions(new byte[Role.PERMISSIONS_STRING_LIMIT]).build()),
                patient = new RoleDTO(Role.builder().name("patient").description("patient").permissions(new byte[Role.PERMISSIONS_STRING_LIMIT]).build());
        admin.setPermissions(List.of(Permission.values()));
        doctor.setPermissions(List.of(Permission.values()));
        patient.setPermissions(List.of(Permission.values()));
        admin = roleService.create(admin);
        doctor = roleService.create(doctor);
        patient = roleService.create(patient);

        DoctorDTO d1 = new DoctorDTO(Doctor.builder()
                .user(User.builder().fullName("Nguyễn Văn A").email("doctor00001@gmail.com").phone("0123456789").password(passwordEncoder.encode("Bach2003")).role(roleService.getEntity(admin.getId())).build())
                .position("Răng hàm mặt").certificate("certificate").gender(Gender.M).build()), d2 = new DoctorDTO(Doctor.builder()
                .user(User.builder().fullName("Nguyễn Văn B").email("doctor00002@gmail.com").phone("0123456689").password(passwordEncoder.encode("Bach2003")).role(roleService.getEntity(doctor.getId())).build())
                .position("Tai mũi họng").certificate("certificate").gender(Gender.F).build());
        d1 = doctorService.create(d1);
        d2 = doctorService.create(d2);
        PatientDTO p1 = new PatientDTO(Patient.builder()
                .user(User.builder().fullName("Nguyễn Văn C").email("patient00001@gmail.com").phone("0122456789").password(passwordEncoder.encode("Bach2003")).role(roleService.getEntity(patient.getId())).build())
                .build()), p2 = new PatientDTO(Patient.builder()
                .user(User.builder().fullName("Nguyễn Văn D").email("patient00002@gmail.com").phone("0124456689").password(passwordEncoder.encode("Bach2003")).role(roleService.getEntity(patient.getId())).build())
                .build());
        p1 = patientService.create(p1);
        p2 = patientService.create(p2);
        ProfileDTO pro1 = new ProfileDTO(Profile.builder().fullName("pro1").gender(Gender.M).dateOfBirth(LocalDate.of(2003, 1, 1)).patient(patientService.getEntity(p1.getId())).build());
        ProfileDTO pro2 = new ProfileDTO(Profile.builder().fullName("pro2").gender(Gender.F).dateOfBirth(LocalDate.of(2003, 1, 1)).patient(patientService.getEntity(p1.getId())).build());
        ProfileDTO pro3 = new ProfileDTO(Profile.builder().fullName("pro3").gender(Gender.M).dateOfBirth(LocalDate.of(2003, 1, 1)).patient(patientService.getEntity(p2.getId())).build());
        ProfileDTO pro4 = new ProfileDTO(Profile.builder().fullName("pro4").gender(Gender.F).dateOfBirth(LocalDate.of(2003, 1, 1)).patient(patientService.getEntity(p2.getId())).build());
        pro1 = profileService.create(pro1);
        pro2 = profileService.create(pro2);
        pro3 = profileService.create(pro3);
        pro4 = profileService.create(pro4);
        List<SchedulerPart> parts = StreamSupport.stream(schedulerService.getParts().spliterator(), false).toList();

        LocalDate date = LocalDate.of(2024, 12, 8);
        SchedulerAllocation allocate = schedulerService.allocate(d1.getId(), date, parts.get(3).start, parts.get(3).end);
        if (allocate != null) {
            AppointmentDTO ap1 = new AppointmentDTO(Appointment.builder().atTime(LocalDateTime.of(date, allocate.atTime)).number(Math.toIntExact(allocate.getId())).profile(profileService.getEntity(pro1.getId())).doctor(doctorService.getEntity(d1.getId())).build());
            ap1 = appointmentService.create(ap1);
        }
        allocate = schedulerService.allocate(d1.getId(), date, parts.get(3).start, parts.get(3).end);
        if (allocate != null) {
            AppointmentDTO ap2 = new AppointmentDTO(Appointment.builder().atTime(LocalDateTime.of(date, allocate.atTime)).number(Math.toIntExact(allocate.getId())).profile(profileService.getEntity(pro2.getId())).doctor(doctorService.getEntity(d1.getId())).build());
            ap2 = appointmentService.create(ap2);
        }
        return ResponseEntity.ok("Hello from secured endpoint");
    }

}