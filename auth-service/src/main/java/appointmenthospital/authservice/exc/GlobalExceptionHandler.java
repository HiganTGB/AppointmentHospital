package appointmenthospital.authservice.exc;

import appointmenthospital.authservice.auth.AuthenticationService;
import appointmenthospital.authservice.log.CustomLogger;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CustomLogger logger;
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        logger.log(ex.getClass().toString(),String.format("Unsupported HTTP method %s for request.Supported method is %s",ex.getMethod(),ex.getHeaders()));
        return "Method '" + ex.getMethod() + "' not supported for this request.";
    }
    @ExceptionHandler({ AppException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExceptionA(AppException e) {
        return e.getMessage();
    }
    @ExceptionHandler({ IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalStateException(IllegalStateException e) {
        return e.getMessage();
    }
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException e) {
        if (e.getBindingResult().hasErrors())
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNumberException(DataIntegrityViolationException e) {
        return e.getMessage();
    }
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex)
    {
        logger.log(ex.getCause().toString(),ex.getMessage());
        return null;
    }
    @ExceptionHandler( BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadCredentials(BadCredentialsException e) {
        logger.log(AuthenticationService.class.toString(),e.getMessage());
        return "Bad Credentials";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Unknow error");
    }
}