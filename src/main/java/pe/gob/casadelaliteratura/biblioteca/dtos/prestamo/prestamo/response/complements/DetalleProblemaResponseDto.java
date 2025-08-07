package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoProblema;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoProblema;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleProblemaResponseDto {

    private String detalleProblema;
    private EstadoProblema estadoProblema;
    private LocalDate fechaProblema;
    private TipoProblema tipoProblema;

}
