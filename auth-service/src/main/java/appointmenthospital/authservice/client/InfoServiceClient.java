package appointmenthospital.authservice.client;

import appointmenthospital.authservice.model.dto.DoctorDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "info-service", path = "https://localhost:10000/api/v1/specialty")
public interface InfoServiceClient {
    @PostMapping("/{id}/add")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean add(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO);
    @PostMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean remove(@PathVariable Long id);
    @PostMapping("/{id}/change")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean change(@PathVariable Long id,Long idSpecialty);
}