package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RangoFechasRequestDto {

    @NotNull(message = "La fecha 'desde' es obligatoria.")
    private LocalDate fechaDesde;

    @NotNull(message = "La fecha 'hasta' es obligatoria.")
    private LocalDate fechaHasta;

}
