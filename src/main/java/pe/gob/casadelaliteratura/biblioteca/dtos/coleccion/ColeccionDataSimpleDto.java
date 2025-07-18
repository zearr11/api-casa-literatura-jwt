package pe.gob.casadelaliteratura.biblioteca.dtos.coleccion;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ColeccionDataSimpleDto {
    private String nombreColeccion;
    private String codigoSala;
}
