package appointmenthospital.appointmentservice.client;

import appointmenthospital.appointmentservice.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service",url = "http://localhost:10001", path = "api/v1/user")
public interface UserServiceClient {
    @GetMapping("/{id}")
    public UserDTO get(@PathVariable(name = "id") Long id);

}