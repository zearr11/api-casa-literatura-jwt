package pe.gob.casadelaliteratura.biblioteca.controllers.persona;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IUsuarioService;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;
import java.util.List;

// FALTA IMPLEMENTAR ALGUNOS MÉTODOS
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final IUsuarioService userService;

    public UsuarioController(IUsuarioService userService) {
        this.userService = userService;
    }

    // http://localhost:8080/api/v1/usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> obtenerUsuariosPorEstado(@RequestParam(required = false)
                                                                             Estado estado) {
        List<UsuarioResponseDto> resultado = userService
                .getAllUsuariosByEstado(
                        (estado == null) ? Estado.ACTIVO : estado
                );
        return ResponseEntity.status(HttpStatus.OK)
                .body(resultado);
    }

    // http://localhost:8080/api/v1/usuarios/codigo/US00001
    @GetMapping("/codigo/{codigoUsuario}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable String codigoUsuario) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getByCodUsuario(codigoUsuario));
    }

    // http://localhost:8080/api/v1/usuarios/numero-doc/12345678
    @GetMapping("/numero-doc/{numDoc}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorNumDoc(@PathVariable String numDoc) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getByNumeroDoc(numDoc));
    }

    // http://localhost:8080/api/v1/usuarios
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarUsuario(@Valid @RequestBody UsuarioRequestDto datosUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveOrUpdate(null, datosUsuario));
    }

    // http://localhost:8080/api/v1/usuarios/US00001
    @PutMapping("/{codigoUsuario}")
    public ResponseEntity<MensajeDto<String>> actualizarUsuario(@PathVariable String codigoUsuario,
                                                                @Valid @RequestBody UsuarioRequestDto datosUsuario) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.saveOrUpdate(codigoUsuario, datosUsuario));
    }

    // http://localhost:8080/api/v1/usuarios/US00001
    @PutMapping("/estado/{codigoUsuario}")
    public ResponseEntity<MensajeDto<String>> habilitarDesabilitarUsuario(@PathVariable String codigoUsuario) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.disableEnableUsuario(codigoUsuario));
    }

}
