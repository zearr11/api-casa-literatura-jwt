package pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ColeccionRequestDto {

    @NotBlank(message = "El campo 'coleccion' es obligatorio.")
    @Size(max = 100, message = "El campo 'coleccion' debe tener como máximo 100 carácteres.")
    private String coleccion;

    @NotBlank(message = "El campo 'codigoSala' es obligatorio.")
    private String codigoSala;

}
