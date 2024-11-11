package appointmenthospital.scheduleservice.controller;

import appointmenthospital.scheduleservice.model.dto.ScheduleDTO;
import appointmenthospital.scheduleservice.model.dto.ScheduleRequest;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import appointmenthospital.scheduleservice.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
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
}
