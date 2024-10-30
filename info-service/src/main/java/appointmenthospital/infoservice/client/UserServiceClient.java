package appointmenthospital.infoservice.client;

import appointmenthospital.infoservice.model.dto.UserDTO;
import appointmenthospital.infoservice.model.dto.UserRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service", path = "https://localhost/api/v1/staff")
public interface UserServiceClient {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO get(@PathVariable(name = "id") String id);
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO update(@PathVariable(name = "id") String id, @Valid @RequestBody UserRequest userRequest);
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO create(@Valid @RequestBody UserRequest userRequest);
}