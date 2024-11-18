package appointmenthospital.appointmentservice.client;


import appointmenthospital.appointmentservice.model.dto.MedicalSpecialtyDTO;
import appointmenthospital.appointmentservice.model.dto.ScheduleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "info-service", contextId = "info-service2",url = "http://localhost:10004", path = "api/v1/specialties")
public interface SpecialtyInfoClient {
    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MedicalSpecialtyDTO getDomain(@PathVariable(name = "id") Long id);
}
