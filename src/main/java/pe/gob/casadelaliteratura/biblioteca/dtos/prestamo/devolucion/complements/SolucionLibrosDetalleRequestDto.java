package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolucionLibrosDetalleRequestDto {

    @NotBlank(message = "El código del libro es obligatorio.")
    private String codigoLibro;

    @NotNull(message = "El número de copia es obligatorio.")
    private Integer numeroCopia;

    @NotNull(message = "El detalle de la solución es obligatorio.")
    private String detalleSolucion;

}
