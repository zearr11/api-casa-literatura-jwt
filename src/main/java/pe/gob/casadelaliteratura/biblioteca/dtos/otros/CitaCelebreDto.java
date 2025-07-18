package pe.gob.casadelaliteratura.biblioteca.dtos.otros;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CitaCelebreDto {

    private String autor;
    private String cita;

}
