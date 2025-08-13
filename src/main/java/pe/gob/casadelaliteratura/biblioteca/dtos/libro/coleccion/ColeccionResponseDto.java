package pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ColeccionResponseDto {

    private String codigo;
    private String coleccion;

}
