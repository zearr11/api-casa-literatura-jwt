package pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataContactoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataUrlDocsDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataPersonalDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteResponseDto {

    private String codigo;
    private DataPersonalDto datosPersonales;
    private DataContactoDto datosContacto;
    private DataUrlDocsDto documentacion;

}
