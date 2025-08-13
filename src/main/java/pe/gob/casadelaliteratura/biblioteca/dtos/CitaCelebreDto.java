package pe.gob.casadelaliteratura.biblioteca.dtos;

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
