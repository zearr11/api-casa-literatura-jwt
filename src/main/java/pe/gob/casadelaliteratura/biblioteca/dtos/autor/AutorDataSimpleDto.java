package pe.gob.casadelaliteratura.biblioteca.dtos.autor;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AutorDataSimpleDto {

    private String nombre;
    private String nacionalidad;

}
