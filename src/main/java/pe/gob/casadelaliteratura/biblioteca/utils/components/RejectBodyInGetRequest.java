package pe.gob.casadelaliteratura.biblioteca.utils.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class RejectBodyInGetRequest extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(request.getMethod())
                && request.getContentLength() > 0) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    """
                    {
                      "fecha": "%s",
                      "mensaje": "El endpoint no solicita que se defina un cuerpo.",
                      "ruta": "%s"
                    }
                    """.formatted(LocalDate.now(), request.getRequestURI())
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}