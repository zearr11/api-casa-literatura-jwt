package pe.gob.casadelaliteratura.biblioteca.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDate;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage<String> resourceNotFoundException(ResourceNotFoundException exception,
                                                          WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                exception.getMessage(),
                request.getDescription(false));
    }

}
