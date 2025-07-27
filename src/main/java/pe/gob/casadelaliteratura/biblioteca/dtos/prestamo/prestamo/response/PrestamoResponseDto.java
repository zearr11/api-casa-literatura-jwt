package pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements.DetallePrestamoResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements.RenovacionesResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements.SancionDemoraResponseDto;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoDevolucion;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrestamoResponseDto {

    private String codigo;
    private LocalDate fechaPrestamo;
    private EstadoDevolucion estadoDevolucion;
    private LocalDate fechaVencimientoOriginal;
    private LocalDate fechaDevolucion; // OPCIONAL

    private UsuarioResponseDto datosUsuario;
    private ClienteResponseDto datosCliente;

    private List<RenovacionesResponseDto> renovaciones; // OPCIONAL

    private List<DetallePrestamoResponseDto> detallePrestamo; // OPCIONAL

    private SancionDemoraResponseDto detalleSancion; // OPCIONAL

}
