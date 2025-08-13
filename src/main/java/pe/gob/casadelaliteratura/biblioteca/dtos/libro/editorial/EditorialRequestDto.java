package pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditorialRequestDto {

    @NotBlank(message = "El campo 'editorial' es obligatorio.")
    @Size(max = 100, message = "El campo 'editorial' debe tener como máximo 100 carácteres.")
    private String editorial;

}
