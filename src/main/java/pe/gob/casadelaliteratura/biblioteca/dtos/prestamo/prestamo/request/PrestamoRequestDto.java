package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.complements.DetallePrestamoDto;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrestamoRequestDto {

    private String codigoCliente;
    private List<DetallePrestamoDto> detallePrestamo;

}
