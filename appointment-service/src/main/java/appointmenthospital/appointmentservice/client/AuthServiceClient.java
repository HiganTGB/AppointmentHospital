package appointmenthospital.appointmentservice.client;

import appointmenthospital.appointmentservice.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service",contextId = "auth-service1",url = "http://localhost:10001", path = "api/v1/users")
public interface AuthServiceClient {
    @GetMapping("/{id}")
    public UserDTO get(@PathVariable(name = "id") Long id);

}