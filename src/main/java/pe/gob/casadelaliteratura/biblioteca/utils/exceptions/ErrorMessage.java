package pe.gob.casadelaliteratura.biblioteca.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage<T> {

    private LocalDate fecha;
    private String mensaje;
    private T detalle;

}
