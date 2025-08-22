package pe.gob.casadelaliteratura.biblioteca.services.impl.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.AuthRequest;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.AuthResponse;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.RefreshRequest;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.RefreshResponse;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.UsuarioRepository;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException401;

@Service
public class AuthService {

    private static final String CREDENTIALS_ERROR = "Credenciales incorrectas.";
    private static final String USER_INACTIVE = "El usuario se encuentra inactivo. Contacte con un administrador.";

    private final UsuarioRepository usuarioRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepo,
                       JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {

        Usuario usuario = usuarioRepo
                .findByPersona_NumeroDoc(request.getNumeroDoc())
                .orElseThrow(() -> new ErrorException401(CREDENTIALS_ERROR));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ErrorException401(CREDENTIALS_ERROR);
        }

        if (!usuario.getEstado().equals(Estado.ACTIVO)) {
            throw new ErrorException401(USER_INACTIVE);
        }

        String accessToken = jwtService.generateAccessToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        return new AuthResponse(accessToken, refreshToken);

    }

    public RefreshResponse refresh(RefreshRequest request) {

        String numeroDoc = jwtService.extractNumeroDoc(request.getRefreshToken());

        Usuario usuario = usuarioRepo.findByPersona_NumeroDoc(numeroDoc)
                .orElseThrow(() -> new ErrorException401("Refresh token inválido."));

        if (!jwtService.isTokenValid(request.getRefreshToken(), usuario)) {
            throw new ErrorException401("Refresh token expirado o inválido.");
        }

        String newAccessToken = jwtService.generateAccessToken(usuario);

        return new RefreshResponse(newAccessToken);

    }

    public Usuario obtenerUsuarioAutenticado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();

    }

}
