package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.ChangePasswordRequest;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    @PostMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean lock(@PathVariable(name = "id") Long id)
    {
        return service.lock(id);
    }
    @DeleteMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean unlock(@PathVariable(name = "id") Long id)
    {
        return service.unlock(id);
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable(name = "id") Long id)
    {
        return service.getDto(id);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}