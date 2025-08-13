package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.complements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePrestamoDto {

    @NotBlank(message = "El código del libro es obligatorio")
    private String codigoLibro;

    @NotNull(message = "El número de copia es obligatorio")
    private Integer numeroCopia;

}
