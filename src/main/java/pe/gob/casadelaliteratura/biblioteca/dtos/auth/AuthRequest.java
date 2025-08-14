package pe.gob.casadelaliteratura.biblioteca.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank(message = "El numero de documento es obligatorio.")
    private String numeroDoc;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

}