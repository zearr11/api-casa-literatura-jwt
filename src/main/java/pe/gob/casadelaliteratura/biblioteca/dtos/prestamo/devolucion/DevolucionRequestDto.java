package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements.DetalleEntregaRequestDto;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevolucionRequestDto {

    @NotBlank(message = "El código del préstamo es obligatorio")
    private String codigoPrestamo;

    @NotEmpty(message = "Debe declararse el detalle de al menos un libro para registrar la devolución.")
    @Valid
    private List<DetalleEntregaRequestDto> detalleLibroDevolucion;

}
