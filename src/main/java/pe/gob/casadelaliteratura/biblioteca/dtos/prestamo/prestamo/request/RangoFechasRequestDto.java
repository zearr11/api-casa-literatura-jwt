package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RangoFechasRequestDto {

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

}
