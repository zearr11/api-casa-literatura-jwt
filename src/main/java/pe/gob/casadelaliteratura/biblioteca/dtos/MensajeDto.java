package pe.gob.casadelaliteratura.biblioteca.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MensajeDto<T> {
    private T mensaje;
}
