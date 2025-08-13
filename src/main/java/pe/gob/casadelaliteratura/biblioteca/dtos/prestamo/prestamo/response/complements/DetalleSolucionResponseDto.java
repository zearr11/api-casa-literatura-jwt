package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleSolucionResponseDto {

    private String detalleSolucion;
    private LocalDate fechaSolucion;

}
