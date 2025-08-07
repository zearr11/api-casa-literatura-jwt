package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.complements.DetallePrestamoDto;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrestamoRequestDto {

    @NotBlank(message = "El código del cliente es obligatorio.")
    private String codigoCliente;

    @NotEmpty(message = "El detalle de préstamo es obligatorio.")
    @Valid
    private List<DetallePrestamoDto> detallePrestamo;

}
