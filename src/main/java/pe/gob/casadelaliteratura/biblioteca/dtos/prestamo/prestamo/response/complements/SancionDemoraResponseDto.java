package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SancionDemoraResponseDto {

    private Integer diasSuspension;
    private LocalDate fechaInicioSancion;
    private LocalDate fechaFinSancion;
    private String estadoSancion;

}
