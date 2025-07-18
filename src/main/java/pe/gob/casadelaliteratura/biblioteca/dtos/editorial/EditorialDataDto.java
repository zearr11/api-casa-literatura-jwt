package pe.gob.casadelaliteratura.biblioteca.dtos.editorial;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditorialDataDto {
    private String codigo;
    private String nombreEditorial;
}
