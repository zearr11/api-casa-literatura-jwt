package pe.gob.casadelaliteratura.biblioteca.services.impl.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final int EXPIRATION_DURATION = 86400000;

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(usuario.getPersona().getNumeroDoc())
                .claim("role", "ROLE_" + usuario.getRol().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DURATION))
                .compact();
    }

    public String extractNumeroDoc(String token) {
        return extractAllClaims(token)
                .getSubject(); // numero de documento
    }

    public boolean isTokenValid(String token, Usuario usuario) {
        String numeroDoc = extractNumeroDoc(token);
        return (numeroDoc.equals(usuario.getPersona().getNumeroDoc()))
                &&
                !isTokenExpired(token); // Si el numero de documento coincide y el token no ha expirado
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody(); // Retorna un objeto Claims que contiene la información del token
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date()); // Devuelve true si el token ha expirado
    }

}
