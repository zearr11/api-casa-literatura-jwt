package pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibroDetalleNuevoRequestDto {

    @NotBlank(message = "El campo 'isbn' es obligatorio.")
    @Size(max = 50, message = "El campo 'isbn' debe tener como máximo 50 carácteres.")
    private String isbn;

    @NotBlank(message = "El campo 'titulo' es obligatorio.")
    @Size(max = 150, message = "El campo 'titulo' debe tener como máximo 150 carácteres.")
    private String titulo;

    @NotNull(message = "El campo 'year' es obligatorio.")
    @Min(value = 1000, message = "El año no puede ser menor a 1000.")
    @Max(value = 2025, message = "El año no puede ser mayor a 2025.")
    private Integer year;

    @NotBlank(message = "El campo 'codigoAutor' es obligatorio.")
    private String codigoAutor;

    @NotBlank(message = "El campo 'codigoColeccion' es obligatorio.")
    private String codigoColeccion;

    @NotBlank(message = "El campo 'codigoEditorial' es obligatorio.")
    private String codigoEditorial;

}
