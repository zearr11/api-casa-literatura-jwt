package pe.gob.casadelaliteratura.biblioteca.utils.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController {

    // Datos invalidos, mal formados, formato incorrecto, campos requeridos faltantes
    @ExceptionHandler(ErrorException400.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage<String> error400(ErrorException400 exception,
                                         WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                exception.getMessage(),
                request.getDescription(false));
    }

    /*
    @ExceptionHandler(Error2Exception400.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage<String> error400Customized(Error2Exception400 exception,
                                                   WebRequest request) {
        return new ErrorMessage<>(
                LocalDate.now(),
                exception.getMessage(),
                exception.getDetalle()
        );
    }
    */

    // Credenciales de autenticación no válidas
    @ExceptionHandler(ErrorException401.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage<String> error401(ErrorException401 exception,
                                         WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                exception.getMessage(),
                request.getDescription(false));
    }

    // Recurso no encontrado
    @ExceptionHandler(ErrorException404.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage<String> error404(ErrorException404 exception,
                                         WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                exception.getMessage(),
                request.getDescription(false));
    }

    // Conflicto con el estado actual del recurso Ejm: Registro Duplicado
    @ExceptionHandler(ErrorException409.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage<String> error409(ErrorException409 exception,
                                         WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                exception.getMessage(),
                request.getDescription(false));
    }

    // ------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage<List<String>> controlValidacionErrores(MethodArgumentNotValidException ex) {

        List<String> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        String.format("Campo '%s': %s", error.getField(),
                                error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorMessage<>(
                LocalDate.now(),
                "Error de validación en los campos proporcionados.",
                detalles
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage<List<String>> handleInvalidFormat(HttpMessageNotReadableException ex) {

        List<String> detalles = new ArrayList<>();

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            String campo = invalidFormat.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));

            String tipoEsperado = invalidFormat.getTargetType().getSimpleName();
            String valorRecibido = invalidFormat.getValue().toString();

            detalles.add(String.format("Campo '%s': valor '%s' no es válido. Se esperaba un valor de tipo '%s'.",
                    campo, valorRecibido, tipoEsperado));

            if (invalidFormat.getTargetType().isEnum()) {
                String[] valoresPermitidos = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .toArray(String[]::new);

                detalles.add("Valores permitidos: " + String.join(", ", valoresPermitidos));
            }

        } else {
            detalles = null;
        }

        return new ErrorMessage<>(
                LocalDate.now(),
                "El cuerpo de la solicitud no se ha definido correctamente.",
                detalles
        );
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage<String> handleMultipartException(MultipartException ex,
                                                         WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                "Error al procesar multipart/form-data.",
                ex.getMessage());
    }

}
