package pe.gob.casadelaliteratura.biblioteca.dtos.cliente;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements.ClienteDataContactoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements.ClienteDataDocDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements.ClienteDataPersonalDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDataDto {

    private String codigo;
    private ClienteDataPersonalDto datosPersonales;
    private ClienteDataContactoDto datosContacto;
    private ClienteDataDocDto documentacion;

}
