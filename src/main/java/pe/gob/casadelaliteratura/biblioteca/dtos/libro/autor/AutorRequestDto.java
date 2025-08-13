package pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AutorRequestDto {

    @NotBlank(message = "El campo 'nombre' es obligatorio.")
    @Size(max = 100, message = "El campo 'nombre' debe tener como máximo 100 carácteres.")
    private String nombre;

    @NotBlank(message = "El campo 'nacionalidad' es obligatorio.")
    @Size(max = 100, message = "El campo 'nacionalidad' debe tener como máximo 100 carácteres.")
    private String nacionalidad;

}
