package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.AppointmentDTO;
import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/scheduler")
@RequiredArgsConstructor
public class SchedulerController {
    private final SchedulerService schedulerService;

    @GetMapping("/part")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SchedulerPart> getAll() {
        return StreamSupport.stream(schedulerService.getParts().spliterator(), false).toList();
    }
}
