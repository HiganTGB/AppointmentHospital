package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.entity.Appointment;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Profile;
import appointmenthospital.authservice.model.entity.SchedulerAllocation;
import appointmenthospital.authservice.service.AppointmentService;
import appointmenthospital.authservice.service.DoctorService;
import appointmenthospital.authservice.service.ProfileService;
import appointmenthospital.authservice.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class  AppointmentController {
    private final SchedulerService schedulerService;
    private final DoctorService doctorService;
    private final ProfileService profileService;
    private AppointmentService appointmentService;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentDTO get(@PathVariable Long id) {
        return appointmentService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentDTO create(@RequestParam(value = "date") LocalDate date,
                                 @RequestParam(value = "start") LocalTime start_time,
                                 @RequestParam(value = "end") LocalTime end_time,
                                 @RequestParam(value = "doctor") long doctor_id,
                                 @RequestParam(value = "profile") long profile_id) {
        Doctor doctor = doctorService.getEntity(doctor_id);
        Profile profile = profileService.getEntity(profile_id);
        SchedulerAllocation allocate = schedulerService.allocate(doctor_id, date, start_time, end_time);
        if (allocate == null) throw new IllegalStateException("Allocate failed");
        AppointmentDTO ap1 = new AppointmentDTO(Appointment.builder().atTime(LocalDateTime.of(date, allocate.atTime)).number(Math.toIntExact(allocate.getId())).profile(profile).doctor(doctor).build());
        ap1 = appointmentService.create(ap1);
        return ap1;
    }

}
