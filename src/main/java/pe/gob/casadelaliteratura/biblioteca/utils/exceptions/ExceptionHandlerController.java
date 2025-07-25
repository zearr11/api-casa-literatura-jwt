package pe.gob.casadelaliteratura.biblioteca.utils.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    // Credenciales de autenticación no válidas
    @ExceptionHandler(ErrorException401.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage<String> error401(ErrorException401 exception,
                                         WebRequest request) {
        return new ErrorMessage<>(LocalDate.now(),
                exception.getMessage(),
                request.getDescription(false));
    }

    // Autenticado, pero sin permisos para acceder a un recurso
    @ExceptionHandler(ErrorException403.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage<String> error403(ErrorException403 exception,
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

    // Cuando el metodo Http no esta permitido para el recurso
    @ExceptionHandler(ErrorException405.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorMessage<String> error405(ErrorException405 exception,
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
    public ResponseEntity<Map<String, Object>> controlValidacionErrores(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("mensaje", "Error de validación en los campos proporcionados.");

        List<String> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        String.format("Campo '%s': %s", error.getField(),
                                error.getDefaultMessage()))
                .collect(Collectors.toList());

        errors.put("detalles", detalles);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("mensaje", "El cuerpo de la solicitud no se ha definido correctamente.");

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            String campo = invalidFormat.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));

            String tipoEsperado = invalidFormat.getTargetType().getSimpleName();
            String valorRecibido = invalidFormat.getValue().toString();

            error.put("detalles",
                    List.of(String.format("Campo '%s': valor '%s' no es válido. Se esperaba un valor de tipo '%s'.",
                    campo, valorRecibido, tipoEsperado)));

            if (invalidFormat.getTargetType().isEnum()) {
                String[] valoresPermitidos = Arrays.stream(invalidFormat.getTargetType()
                                .getEnumConstants())
                        .map(Object::toString)
                        .toArray(String[]::new);
                error.put("valoresPermitidos", Arrays.asList(valoresPermitidos));
            }
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("mensaje", "Error interno del servidor.");
        error.put("detalle", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorMessage<String>> handleMultipartException(MultipartException ex,
                                                                       WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage<>(LocalDate.now(),
                        "Error al procesar multipart/form-data. Validar imágenes enviadas.",
                        request.getDescription(false)));
    }

}
