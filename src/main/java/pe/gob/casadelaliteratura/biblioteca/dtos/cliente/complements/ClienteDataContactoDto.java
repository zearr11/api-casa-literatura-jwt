package pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDataContactoDto {

    private String numeroPrincipal;
    private String numeroSecundario;
    private String correo;

}
