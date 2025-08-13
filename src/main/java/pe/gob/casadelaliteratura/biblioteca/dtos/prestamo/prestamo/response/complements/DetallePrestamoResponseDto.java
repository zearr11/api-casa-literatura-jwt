package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetallePrestamoResponseDto {

    private String codigoLibro;
    private String titulo;
    private Integer numeroCopia;

    private DetalleDevolucionResponseDto detalleDevolucion; // OPCIONAL (TABLA DETALLE_DEVOLUCION)

}
