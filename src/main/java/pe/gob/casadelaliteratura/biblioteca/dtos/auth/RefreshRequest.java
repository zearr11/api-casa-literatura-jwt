package pe.gob.casadelaliteratura.biblioteca.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshRequest {

    @NotBlank(message = "El refreshToken es obligatorio.")
    private String refreshToken;

}
