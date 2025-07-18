package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.complements.DetallePrestamoDataDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrestamoDataDto {

    private String codCliente;
    private List<DetallePrestamoDataDto> detallePrestamo;

}
