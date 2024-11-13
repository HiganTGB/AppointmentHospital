package appointmenthospital.authservice.controller;

import appointmenthospital.authservice.model.dto.ChangePasswordRequest;
import appointmenthospital.authservice.model.dto.UserDTO;
import appointmenthospital.authservice.model.dto.UserRequest;
import appointmenthospital.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.core.annotation.Order;
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

@Tag(name = "User API", description = "All about User")
@Order(2)
public class UserController {

    private final UserService service;
    @Operation(summary = "Lock", description = "Lock account")
    @PostMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean lock(@PathVariable(name = "id") Long id)
    {
        return service.lock(id);
    }
    @Operation(summary = "Unlock", description = "Unlock account")
    @DeleteMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean unlock(@PathVariable(name = "id") Long id)
    {
        return service.unlock(id);
    }
    @Operation(summary = "Password", description = "Change password", tags = { "Need Principal" })
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "update Profile", description = "update User", tags = { "Need Principal" })
    @PutMapping
    public UserDTO update(
            @Valid @RequestBody UserRequest userRequest,
            Principal connectedUser)
    {
            return service.update(userRequest,connectedUser);
    }
}