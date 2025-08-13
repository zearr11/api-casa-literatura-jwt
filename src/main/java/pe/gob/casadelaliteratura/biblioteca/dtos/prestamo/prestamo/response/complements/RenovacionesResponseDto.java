package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.MedioSolicitud;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RenovacionesResponseDto {

    private Integer numeroSolicitud;
    private LocalDate fechaSolicitud;
    private MedioSolicitud medioSolicitud;
    private LocalDate nuevaFechaVencimientoAsignada;

}
