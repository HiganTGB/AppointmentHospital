package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.exc.AppException;
import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.model.entity.Appointment;
import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.Profile;
import appointmenthospital.authservice.model.entity.SchedulerAllocation;
import appointmenthospital.authservice.payment.PaymentDTO;
import appointmenthospital.authservice.payment.PaymentService;
import appointmenthospital.authservice.service.*;
import appointmenthospital.authservice.service.DoctorService;
import appointmenthospital.authservice.service.ProfileService;
import appointmenthospital.authservice.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final SchedulerService schedulerService;
    private final DoctorService doctorService;
    private final ProfileService profileService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentDTO get(@PathVariable Long id) {
        return appointmentService.get(id);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public Page<AppointmentDTO> getAll(@RequestParam(defaultValue = "", value = "search", required = false) String keyword,
                                       @RequestParam(defaultValue = "0", value = "page", required = false) int page,
                                       @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
                                       @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy,
                                       @RequestParam(value = "doctor", required = false) Long doctor_id,
                                       @RequestParam(value = "profile", required = false) Long profile_id,
                                       @RequestParam(value = "patient", required = false) Long patient_id

    ) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return appointmentService.getPaged(keyword, pageable, doctor_id, profile_id, patient_id);
    }

    @GetMapping("/user/current")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Page", description = "Get page + search by keyword")
    public List<AppointmentDTO> getAll(@RequestParam(defaultValue = "", value = "search", required = false) String keyword,
                                       @RequestParam(defaultValue = "0", value = "page", required = false) int page,
                                       @RequestParam(value = "sortBy", required = false, defaultValue = "atTime") String sortBy,
                                       @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy,
                                       Principal connectedUser

    ) {
        Sort sort = Sort.by(orderBy.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, 10, sort); // Assuming a page size of 10

        return appointmentService.getPaged(keyword, pageable, connectedUser).getContent();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentDTO create(@RequestParam(value = "date") LocalDate date,
                                           @RequestParam(value = "start") LocalTime start_time,
                                           @RequestParam(value = "end") LocalTime end_time,
                                           @RequestParam(value = "doctor") long doctor_id,
                                           @RequestParam(value = "profile") long profile_id,
                                           HttpServletRequest request) {
        Doctor doctor = doctorService.getEntity(doctor_id);
        Profile profile = profileService.getEntity(profile_id);
        SchedulerAllocation allocate = schedulerService.allocate(doctor_id, date, start_time, end_time);
        if (allocate == null) throw new IllegalStateException("Allocate failed");
        AppointmentDTO ap1 = new AppointmentDTO(Appointment.builder().atTime(LocalDateTime.of(date, allocate.atTime)).number(Math.toIntExact(allocate.getId())).profile(profile).doctor(doctor).build());
        ap1 = appointmentService.create(ap1,request);
        return ap1;
    }

    @GetMapping("/vn-pay-callback")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Boolean payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String id=request.getParameter("vnp_TxnRef");
        if (status.equals("00")) {
            return  appointmentService.pay(Long.parseLong(id));
        } else {
            return false;
        }
    }

//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public AppointmentDTO update(@PathVariable Long id,@RequestBody @Valid AppointmentDTO dto)
//    {
//        return appointmentService.update(dto,id);
//    }
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Boolean delete(@PathVariable Long id)
//    {
//        return appointmentService.delete(id);
//    }
}
