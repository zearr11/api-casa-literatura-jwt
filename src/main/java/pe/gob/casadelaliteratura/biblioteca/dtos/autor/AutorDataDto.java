package pe.gob.casadelaliteratura.biblioteca.dtos.autor;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AutorDataDto {

    private String codigo;
    private String nombre;
    private String nacionalidad;

}
