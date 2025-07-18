package pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements;

import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoDoc;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDataPersonalDto {

    private String nombres;
    private String apellidos;
    private TipoDoc tipoDocumento;
    private String numeroDoc;
    private LocalDate fechaNacimiento;
    private String direccion;

}
