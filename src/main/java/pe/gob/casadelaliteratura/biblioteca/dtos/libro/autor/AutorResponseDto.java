package pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AutorResponseDto {

    private String codigo;
    private String nombre;
    private String nacionalidad;

}
