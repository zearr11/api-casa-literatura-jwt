package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoEntrega;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoProblema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetalleDevolucionResponseDto {

    private TipoEntrega tipoEntrega;

    // Opcional
    private String detalleProblema;
    private String estadoProblema;
    private String fechaProblema;
    private TipoProblema tipoProblema;

    // Opcional
    private String detalleSolucion;
    private String fechaSolucion;

}
