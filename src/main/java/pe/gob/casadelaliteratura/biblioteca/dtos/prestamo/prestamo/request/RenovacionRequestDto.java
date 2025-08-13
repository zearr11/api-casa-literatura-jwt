package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.MedioSolicitud;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenovacionRequestDto {

    @NotBlank(message = "El campo 'codPrestamo' es obligatorio.")
    private String codPrestamo;

    @NotNull(message = "El campo 'medioSolicitud' es obligatorio.")
    private MedioSolicitud medioSolicitud;

}
