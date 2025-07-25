package pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataContactoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataPersonalDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteRequestDto {

    @Valid
    @NotNull(message = "Los datos personales son obligatorios.")
    private DataPersonalDto datosPersonales;

    @Valid
    @NotNull(message = "Los datos de contacto son obligatorios.")
    private DataContactoDto datosContacto;

}
