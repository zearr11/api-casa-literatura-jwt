package pe.gob.casadelaliteratura.biblioteca.dtos.sala;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SalaData2Dto {

    private String codigo;
    private String nombreSala;
    private List<ColeccionDataDto> colecciones;

}
