package appointmenthospital.scheduleservice.controller;

import appointmenthospital.scheduleservice.model.dto.AvailableDateDTO;
import appointmenthospital.scheduleservice.model.dto.ScheduleDTO;
import appointmenthospital.scheduleservice.model.dto.ScheduleRequest;
import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import appointmenthospital.scheduleservice.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@Tag(name = "Schedule API", description = "All about schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ScheduleDTO create(@Valid @RequestBody ScheduleRequest scheduleRequest)
    {
        return scheduleService.create(scheduleRequest);
    }
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ScheduleDTO update(@Valid @RequestBody ScheduleRequest scheduleRequest,@PathVariable Long id)
    {
        return scheduleService.update(scheduleRequest,id);
    }
    @GetMapping("/room/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get schedule by Room", description = "Get all schedule from monday to sunday,morning or evening by room")
    public List<ScheduleDTO> getByRoom(@PathVariable Long id) {
        return scheduleService.getByRoom(id);
    }
    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Boolean delete(@PathVariable Long id)
    {
        return scheduleService.delete(id);
    }

    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ScheduleDTO get(@PathVariable Long id)
    {
        return scheduleService.getDomain(id);
    }

    @GetMapping("/public/available")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Available Date by specialty", description = "Get all Available Date from monday to sunday by specialty",tags = {"Public"})
    public List<AvailableDateDTO> getBySpecialty( @RequestParam(value = "specialty", required = true) Long specialtyID)
    {
        return scheduleService.getAvailableDateBySpecialty(specialtyID);
    }
    @GetMapping("/public/schedule")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get schedule by date,specialty", description = "Get all Available Date from monday to sunday by specialty",tags = {"Public"})
    public List<ScheduleDTO> getByDayAndSpecialty(@RequestParam(value = "dayofweek", required = true) int dayOfWeek,
                                                  @RequestParam(value = "specialty", required = true) Long specialtyID)
    {
        return scheduleService.getBySpecialtyAndDayOfWeek(specialtyID, DayOfWeek.getDayOfWeekFromInt(dayOfWeek));
    }


}
