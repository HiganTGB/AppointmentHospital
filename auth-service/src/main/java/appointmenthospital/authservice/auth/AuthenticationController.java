package appointmenthospital.authservice.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Login/register/refresh-token")
@Order(1)
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "Register", description = "Register Customer,Returns access token,refresh token",tags = {"Customer API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Register", content = @Content(schema = @Schema(implementation = RegisterRequest.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
       @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @Operation(summary = "Login", description = "Returns access token,refresh token"  )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = @Content(schema = @Schema(implementation = AuthenticationRequest.class))),
            @ApiResponse(responseCode = "400", description = "Bad Credentials", content = @Content),
               })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}