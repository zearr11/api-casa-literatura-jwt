package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.complements;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePrestamoDataDto {
    private String codLibro;
    private Integer numeroCopia;
}
