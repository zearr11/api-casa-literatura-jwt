package pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoDoc;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataPersonalDto {

    @NotBlank(message = "El campo 'nombre' es obligatorio.")
    @Size(max = 100, message = "El campo 'nombre' debe tener como máximo 100 carácteres.")
    private String nombres;

    @NotBlank(message = "El campo 'apellido' es obligatorio.")
    @Size(max = 100, message = "El campo 'apellido' debe tener como máximo 100 carácteres.")
    private String apellidos;

    @NotNull(message = "El campo 'tipo de documento' es obligatorio.")
    private TipoDoc tipoDocumento;

    @NotBlank(message = "El campo 'numero de documento' es obligatorio.")
    @Size(min = 8, max = 11, message = "El campo 'numero de documento' debe tener entre 8 y 11 carácteres.")
    private String numeroDoc;

    @NotNull(message = "El campo 'fecha de nacimiento' es obligatorio.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El campo 'dirección' es obligatorio.")
    @Size(max = 150, message = "El campo 'dirección' debe tener como máximo 150 carácteres.")
    private String direccion;

}
