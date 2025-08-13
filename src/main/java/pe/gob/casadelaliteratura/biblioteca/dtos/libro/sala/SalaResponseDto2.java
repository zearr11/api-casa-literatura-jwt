package pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionResponseDto;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SalaResponseDto2 {

    private String codigo;
    private String sala;
    private List<ColeccionResponseDto> colecciones;

}
