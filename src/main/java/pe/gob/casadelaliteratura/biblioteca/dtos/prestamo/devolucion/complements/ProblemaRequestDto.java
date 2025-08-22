package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoProblema;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemaRequestDto {

    @NotBlank(message = "El detalle del problema es obligatorio.")
    private String detalleProblema;

    @NotNull(message = "El tipo de problema es obligatorio")
    private TipoProblema tipoProblema;

}
