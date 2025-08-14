package pe.gob.casadelaliteratura.biblioteca.utils.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.UsuarioRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.auth.JwtService;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt;
        String numeroDoc;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        numeroDoc = jwtService.extractNumeroDoc(jwt);

        if (numeroDoc != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Usuario usuario = userRepository.findByPersona_NumeroDoc(numeroDoc)
                    .orElse(null);

            if (usuario != null && jwtService.isTokenValid(jwt, usuario)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                usuario, null,
                                List.of(
                                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
                                )
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
