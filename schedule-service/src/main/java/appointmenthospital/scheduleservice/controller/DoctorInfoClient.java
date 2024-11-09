package appointmenthospital.scheduleservice.controller;

import appointmenthospital.scheduleservice.model.dto.DoctorDomain;
import appointmenthospital.scheduleservice.model.dto.RoomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "info-service",url = "http://localhost:10001", path = "api/v1/doctor")
public interface DoctorInfoClient {
    @GetMapping("/{id}/domain")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DoctorDomain getDomain(@PathVariable Long id);
}
