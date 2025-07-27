package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import pe.gob.casadelaliteratura.biblioteca.utils.enums.MedioSolicitud;
import java.time.LocalDate;

public class RenovacionesResponseDto {

    private String numeroSolicitud;
    private LocalDate fechaSolicitud;
    private MedioSolicitud medioSolicitud;
    private LocalDate nuevaFechaVencimientoAsignada;

}
