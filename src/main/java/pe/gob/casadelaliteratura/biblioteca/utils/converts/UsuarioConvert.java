package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataContactoDto2;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataPersonalDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections.UsuarioProjection;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Role;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoDoc;

public class UsuarioConvert {

    public static UsuarioResponseDto usuarioToUsuarioDataResponseDto(Usuario usuario) {

        String codigo = usuario.getCodUsuario();
        Role role = usuario.getRol();

        DataPersonalDto dataPersonal = new DataPersonalDto(
                usuario.getPersona().getNombres(),
                usuario.getPersona().getApellidos(),
                usuario.getPersona().getTipoDoc(),
                usuario.getPersona().getNumeroDoc(),
                usuario.getPersona().getFechaNacimiento(),
                usuario.getPersona().getDireccion()
        );

        DataContactoDto2 dataContacto = new DataContactoDto2(
                usuario.getPersona().getNumeroPrincipal(),
                usuario.getPersona().getCorreo()
        );

        return new UsuarioResponseDto(
                codigo, role, dataPersonal, dataContacto
        );

    }

    public static UsuarioResponseDto usuarioProjectionToUsuarioDataResponseDto(UsuarioProjection usuario) {

        String codigo = usuario.getCodUsuario();
        Role role = Role.valueOf(usuario.getRol());

        DataPersonalDto dataPersonal = new DataPersonalDto(
                usuario.getNombres(),
                usuario.getApellidos(),
                TipoDoc.valueOf(usuario.getTipoDoc()),
                usuario.getNumeroDoc(),
                usuario.getFechaNacimiento(),
                usuario.getDireccion()
        );

        DataContactoDto2 dataContacto = new DataContactoDto2(
                usuario.getNumeroPrincipal(),
                usuario.getCorreo()
        );

        return new UsuarioResponseDto(
                codigo, role, dataPersonal, dataContacto
        );

    }

}
