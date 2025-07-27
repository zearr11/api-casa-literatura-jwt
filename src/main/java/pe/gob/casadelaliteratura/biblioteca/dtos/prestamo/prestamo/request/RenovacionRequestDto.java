package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.MedioSolicitud;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenovacionRequestDto {

    private String codPrestamo;
    private MedioSolicitud medioSolicitud;

}
