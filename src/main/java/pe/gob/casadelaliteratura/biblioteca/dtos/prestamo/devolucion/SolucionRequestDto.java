package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements.SolucionLibrosDetalleRequestDto;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolucionRequestDto {

    @NotBlank(message = "El código del préstamo es obligatorio")
    private String codigoPrestamo;

    @NotEmpty(message = "Debe declararse el detalle de solución de al menos un libro para registrar la solución.")
    @Valid
    private List<SolucionLibrosDetalleRequestDto> detalleSolucion;

}
