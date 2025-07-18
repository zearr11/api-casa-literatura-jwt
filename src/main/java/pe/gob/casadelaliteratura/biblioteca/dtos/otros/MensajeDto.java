package pe.gob.casadelaliteratura.biblioteca.dtos.otros;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MensajeDto<T> {
    private T mensaje;
}
