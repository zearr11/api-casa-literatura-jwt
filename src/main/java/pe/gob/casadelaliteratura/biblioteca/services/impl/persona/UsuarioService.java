package pe.gob.casadelaliteratura.biblioteca.services.impl.persona;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.UsuarioRepository;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IUsuarioService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.UsuarioConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoUsuario;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository userRepository;

    public UsuarioService(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MensajeDto<String> saveOrUpdate(String codUsuario, UsuarioRequestDto datosUsuario) {
        return new MensajeDto<>("Prueba OK");
    }

    @Override
    public List<UsuarioResponseDto> getAllUsuariosByEstado(EstadoUsuario estado) {
        return userRepository.findByEstado(estado).orElseThrow(() ->
                        new ErrorException404("No se encontraron usuarios.")
                )
                .stream().map(
                        UsuarioConvert::usuarioToUsuarioDataResponseDto
                )
                .toList();
    }

    @Override
    public UsuarioResponseDto getByCodUsuario(String codUsuario) {
        return userRepository.findByCustomized(null, codUsuario, null)
                .map(UsuarioConvert::usuarioProjectionToUsuarioDataResponseDto)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró al usuario con el codigo: " + codUsuario
                ));
    }

    @Override
    public UsuarioResponseDto getByCodPersona(String codPersona) {
        return null;
    }

    @Override
    public UsuarioResponseDto getByNumeroDoc(String numeroDoc) {
        return null;
    }

}
