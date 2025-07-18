package pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.request;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoLibro;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibroCopiaNuevoDto {

    private String codigoLibro;
    private EstadoLibro estadoLibro;

}
