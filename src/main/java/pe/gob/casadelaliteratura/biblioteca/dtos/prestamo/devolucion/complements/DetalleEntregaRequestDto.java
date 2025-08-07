package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoEntrega;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleEntregaRequestDto {

    @NotBlank(message = "El código del libro es obligatorio.")
    private String codigoLibro;

    @NotNull(message = "El número de copia es obligatorio.")
    private Integer numeroCopia;

    @NotNull(message = "El tipo de entrega es obligatorio")
    private TipoEntrega tipoEntrega;

    @Valid
    private ProblemaRequestDto detalleProblema; // OPCIONAL

}
