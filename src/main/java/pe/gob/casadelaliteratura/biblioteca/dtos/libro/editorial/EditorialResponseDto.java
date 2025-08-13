package pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditorialResponseDto {

    private String codigo;
    private String editorial;

}
