package appointmenthospital.appointmentservice.client;


import appointmenthospital.appointmentservice.model.dto.RoomDomain;
import appointmenthospital.appointmentservice.model.dto.ScheduleDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "schedule-service",url = "http://localhost:10002", path = "api/v1/schedule")
public interface ScheduleInfoClient {
    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ScheduleDTO get(@PathVariable Long id);
}
