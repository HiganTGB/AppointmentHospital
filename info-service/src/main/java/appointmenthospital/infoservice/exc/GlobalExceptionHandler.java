package appointmenthospital.infoservice.exc;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({PropertyReferenceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExceptionProperty(PropertyReferenceException e)
    {
        return null;
    }
    @ExceptionHandler({ ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExceptionA(ItemNotFoundException e) {
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
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNumberException(NumberFormatException e) {
        return null;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Unknow error");
    }
}