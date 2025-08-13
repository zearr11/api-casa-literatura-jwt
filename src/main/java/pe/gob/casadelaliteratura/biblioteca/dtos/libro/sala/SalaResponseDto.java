package pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SalaResponseDto {

    private String codigo;
    private String sala;

}
