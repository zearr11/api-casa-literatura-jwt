package pe.gob.casadelaliteratura.biblioteca.services.impl.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.login.LoginRequest;
import pe.gob.casadelaliteratura.biblioteca.dtos.login.LoginResponse;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.UsuarioRepository;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException401;

@Service
public class LoginService {

    private static final String CREDENTIALS_ERROR = "Credenciales incorrectas.";
    private static final String USER_INACTIVE = "El usuario se encuentra inactivo. Contacte con un administrador.";

    private final UsuarioRepository usuarioRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UsuarioRepository usuarioRepo,
                        JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepo
                .findByPersona_NumeroDoc(request.getNumeroDoc())
                .orElseThrow(() -> new ErrorException401(CREDENTIALS_ERROR));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ErrorException401(CREDENTIALS_ERROR);
        }

        if (!usuario.getEstado().equals(Estado.ACTIVO)) {
            throw new ErrorException401(USER_INACTIVE);
        }

        String jwt = jwtService.generateToken(usuario);

        return new LoginResponse(jwt);

    }

    public Usuario obtenerUsuarioAutenticado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();

    }

}
