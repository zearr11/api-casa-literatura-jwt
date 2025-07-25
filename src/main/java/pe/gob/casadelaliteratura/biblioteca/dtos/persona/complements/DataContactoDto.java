package pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataContactoDto {

    @NotBlank(message = "El campo 'numero principal' es obligatorio.")
    @Size(min= 9, max = 9, message = "El campo 'numero principal' debe tener 9 carácteres.")
    private String numeroPrincipal;

    @NotBlank(message = "El campo 'numero secundario' es obligatorio.")
    @Size(min= 9, max = 9, message = "El campo 'numero secundario' debe tener 9 carácteres.")
    private String numeroSecundario;

    @NotBlank(message = "El campo 'correo' es obligatorio.")
    @Size(max = 100, message = "El campo 'correo' debe tener como máximo 100 carácteres.")
    @Email(message = "El campo 'correo' debe tener un formato válido.")
    private String correo;

}
