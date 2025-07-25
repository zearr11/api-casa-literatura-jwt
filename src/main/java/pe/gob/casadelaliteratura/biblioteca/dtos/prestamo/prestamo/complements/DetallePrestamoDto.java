package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.complements;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePrestamoDto {

    private String codigoLibro;
    private Integer numeroCopia;

}
