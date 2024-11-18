package appointmenthospital.scheduleservice.client;

import appointmenthospital.scheduleservice.model.domain.DoctorDomain;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "auth-service",url = "http://localhost:10001", path = "api/v1/doctors")
public interface DoctorInfoClient {
    @GetMapping("/domain/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DoctorDomain getDomain(@PathVariable Long id);
}
