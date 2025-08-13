package pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataContactoDto2;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataPersonalDto;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioResponseDto {

    private String codigo;
    private Role rol;
    private DataPersonalDto datosPersonales;
    private DataContactoDto2 datosContacto;

}
