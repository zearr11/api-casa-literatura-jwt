package pe.gob.casadelaliteratura.biblioteca.services.impl.persona;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Persona;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.PersonaRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.UsuarioRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections.UsuarioProjection;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IUsuarioService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.UsuarioConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import pe.gob.casadelaliteratura.biblioteca.utils.validations.ValidacionDocIden;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository userRepository;
    private final AlmacenCodigosService acService;
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository userRepository,
                          AlmacenCodigosService acService,
                          PersonaRepository personaRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.acService = acService;
        this.personaRepository = personaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdate(String codUsuario, UsuarioRequestDto datosUsuario) {
        String msg;

        // Validacion Doc Identidad
        ValidacionDocIden.validationDoc(datosUsuario.getDatosPersonales().getNumeroDoc(),
                datosUsuario.getDatosPersonales().getTipoDocumento());

        UsuarioProjection usuarioExistente = userRepository.findByCustomized(
                        datosUsuario.getDatosPersonales().getNumeroDoc(), null, null)
                .orElse(null);

        if (codUsuario == null) {
            if (usuarioExistente != null)
                throw new ErrorException409("Ya existe un usuario con el numero de documento ingresado.");
            Persona persona = new Persona();
            persona.setCodPersona(acService.generateCodigo("PA"));
            persona.setApellidos(datosUsuario.getDatosPersonales().getApellidos());
            persona.setNombres(datosUsuario.getDatosPersonales().getNombres());
            persona.setDireccion(datosUsuario.getDatosPersonales().getDireccion());
            persona.setFechaNacimiento(datosUsuario.getDatosPersonales().getFechaNacimiento());
            persona.setNumeroDoc(datosUsuario.getDatosPersonales().getNumeroDoc());
            persona.setNumeroPrincipal(datosUsuario.getDatosContacto().getNumeroPrincipal());
            persona.setTipoDoc(datosUsuario.getDatosPersonales().getTipoDocumento());
            persona.setCorreo(datosUsuario.getDatosContacto().getCorreo());

            acService.updateTable("PA");

            persona = personaRepository.save(persona);

            Usuario usuario = new Usuario();
            usuario.setCodUsuario(acService.generateCodigo("US"));
            usuario.setRol(datosUsuario.getRol());

            String contrasenia = datosUsuario.getPassword();
            if (contrasenia == null || contrasenia.isBlank())
                throw new ErrorException409("No se ha definido un valor para el campo 'password'.");

            String contraseniaEncriptada = passwordEncoder.encode(contrasenia);
            usuario.setPassword(contraseniaEncriptada);

            usuario.setEstado(Estado.ACTIVO);
            usuario.setPersona(persona);

            acService.updateTable("US");

            userRepository.save(usuario);

            msg = "Usuario registrado correctamente";
        } else {
            if (usuarioExistente != null && !usuarioExistente.getCodUsuario().equals(codUsuario))
                throw new ErrorException409("Ya existe un usuario con el numero de documento ingresado.");

            Usuario user = userRepository.findById(codUsuario)
                    .orElseThrow(() -> new ErrorException404("No se encontró el usuario con el código: " + codUsuario));
            Persona persona1 = user.getPersona();

            user.setRol(datosUsuario.getRol());

            String nuevaContrasenia = datosUsuario.getPassword();
            if (nuevaContrasenia != null && !nuevaContrasenia.isEmpty()) {
                user.setPassword(passwordEncoder.encode(nuevaContrasenia));
            }

            persona1.setNombres(datosUsuario.getDatosPersonales().getNombres());
            persona1.setApellidos(datosUsuario.getDatosPersonales().getApellidos());
            persona1.setDireccion(datosUsuario.getDatosPersonales().getDireccion());
            persona1.setNumeroPrincipal(datosUsuario.getDatosContacto().getNumeroPrincipal());
            persona1.setCorreo(datosUsuario.getDatosContacto().getCorreo());
            persona1.setFechaNacimiento(datosUsuario.getDatosPersonales().getFechaNacimiento());
            persona1.setNumeroDoc(datosUsuario.getDatosPersonales().getNumeroDoc());
            persona1.setTipoDoc(datosUsuario.getDatosPersonales().getTipoDocumento());

            personaRepository.save(persona1);
            userRepository.save(user);

            msg = "Usuario actualizado correctamente";
        }

        return new MensajeDto<>(msg);
    }

    @Override
    public MensajeDto<String> disableEnableUsuario(String codUsuario) {
        if (codUsuario == null || codUsuario.trim().isEmpty())
            throw new ErrorException409("Debe proporcionar el código de usuario.");

        Usuario usuario = userRepository.findById(codUsuario)
                .orElseThrow(() -> new ErrorException404("No se encontró el usuario con el código: " + codUsuario));

        Estado estadoActual = usuario.getEstado();
        Estado nuevoEstado = (estadoActual == Estado.ACTIVO) ? Estado.INACTIVO : Estado.ACTIVO;
        usuario.setEstado(nuevoEstado);
        userRepository.save(usuario);

        String msg = (nuevoEstado == Estado.ACTIVO)
                ? "Usuario activado correctamente"
                : "Usuario desactivado correctamente";

        return new MensajeDto<>(msg);
    }

    @Override
    public List<UsuarioResponseDto> getAllUsuariosByEstado(Estado estado) {
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
    public UsuarioResponseDto getByNumeroDoc(String numeroDoc) {
        if (numeroDoc == null || numeroDoc.trim().isEmpty())
            throw new ErrorException409("Debe proporcionar el número de documento.");

        return userRepository.findByCustomized(numeroDoc, null, null)
                .map(UsuarioConvert::usuarioProjectionToUsuarioDataResponseDto)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró al usuario con el número de documento: " + numeroDoc
                ));
    }

}
