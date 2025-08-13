package pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataContactoDto2;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataPersonalDto;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioRequestDto {

    @NotNull(message = "El campo 'rol' es obligatorio.")
    private Role rol;

    private String password;

    @Valid
    @NotNull(message = "Los datos personales son obligatorios.")
    private DataPersonalDto datosPersonales;

    @Valid
    @NotNull(message = "Los datos de contacto son obligatorios.")
    private DataContactoDto2 datosContacto;

}
