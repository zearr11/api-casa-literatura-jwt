package pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SalaRequestDto {

    @NotBlank(message = "El campo 'sala' es obligatorio.")
    @Size(max = 100, message = "El campo 'sala' debe tener como máximo 100 carácteres.")
    private String sala;

}
