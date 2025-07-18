package pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDataDocDto {

    private byte[] imgDocIdentidad;
    private byte[] imgRecServicio;

}
