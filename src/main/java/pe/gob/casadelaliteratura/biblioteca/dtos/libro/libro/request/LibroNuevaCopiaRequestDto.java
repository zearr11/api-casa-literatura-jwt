package pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoLibro;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibroNuevaCopiaRequestDto {

    @NotBlank(message = "El campo 'codigoLibro' es obligatorio.")
    private String codigoLibro;

    @NotNull(message = "El campo 'estado' es obligatorio.")
    private EstadoLibro estado;

}
