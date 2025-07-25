package pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoUsuario;

import java.util.List;

public interface IUsuarioService {

    MensajeDto<String> saveOrUpdate(String codUsuario, UsuarioRequestDto datosUsuario);
    List<UsuarioResponseDto> getAllUsuariosByEstado(EstadoUsuario estado);
    UsuarioResponseDto getByCodUsuario(String codUsuario);
    UsuarioResponseDto getByCodPersona(String codPersona);
    UsuarioResponseDto getByNumeroDoc(String numeroDoc);

}
