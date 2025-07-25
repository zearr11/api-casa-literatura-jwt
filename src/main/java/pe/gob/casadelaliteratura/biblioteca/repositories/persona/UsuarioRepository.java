package pe.gob.casadelaliteratura.biblioteca.repositories.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections.UsuarioProjection;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoUsuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    @Query(value = """
            SELECT u.cod_usuario, u.password, u.rol,
            	   p.cod_persona, p.apellidos, p.correo, p.direccion, p.fecha_nacimiento,
            	   p.nombres, p.numero_doc, p.numero_principal, p.tipo_doc
            FROM usuario u
            INNER JOIN persona p ON p.cod_persona = u.fk_cod_persona
            WHERE (:numeroDoc IS NULL OR p.numero_doc = :numeroDoc)
              AND (:codUsuario IS NULL OR u.cod_usuario = :codUsuario)
              AND (:codPersona IS NULL OR p.cod_persona = :codPersona)
            """, nativeQuery = true)
    Optional<UsuarioProjection> findByCustomized(String numeroDoc, String codUsuario, String codPersona);

    Optional<List<Usuario>> findByEstado(EstadoUsuario estado);
}
