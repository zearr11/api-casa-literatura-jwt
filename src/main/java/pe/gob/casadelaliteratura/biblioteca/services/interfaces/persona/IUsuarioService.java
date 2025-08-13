package pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;

import java.util.List;

public interface IUsuarioService {

    MensajeDto<String> saveOrUpdate(String codUsuario, UsuarioRequestDto datosUsuario);
    MensajeDto<String> disableEnableUsuario(String codUsuario);
    List<UsuarioResponseDto> getAllUsuariosByEstado(Estado estado);
    UsuarioResponseDto getByCodUsuario(String codUsuario);
    UsuarioResponseDto getByNumeroDoc(String numeroDoc);

}
