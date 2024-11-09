package appointmenthospital.authservice.client;

import appointmenthospital.authservice.model.dto.DoctorDTO;
import appointmenthospital.authservice.model.dto.DoctorDomain;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "info-service",url = "http://localhost:10004", path = "api/v1/specialty")
public interface InfoServiceClient {
    @PostMapping("/{id}/add")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long add(@PathVariable Long id, @RequestBody DoctorDomain doctorDomain);
    @PostMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean remove(@PathVariable Long id);
}