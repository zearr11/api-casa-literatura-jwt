package pe.gob.casadelaliteratura.biblioteca.repositories.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Cliente;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections.ClienteProjection;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query(value = """
            SELECT c.cod_cliente, c.numero_secundario,
            	   c.url_doc_identidad, c.url_rec_servicio,
                   p.cod_persona, p.apellidos, p.correo, p.direccion, p.fecha_nacimiento,
                   p.nombres, p.numero_doc, p.numero_principal, p.tipo_doc
            FROM cliente c
            INNER JOIN persona p ON p.cod_persona = c.fk_cod_persona
            WHERE (:numeroDoc IS NULL OR p.numero_doc = :numeroDoc)
              AND (:codCliente IS NULL OR c.cod_cliente = :codCliente)
              AND (:codPersona IS NULL OR p.cod_persona = :codPersona)
            """, nativeQuery = true)
    Optional<ClienteProjection> findByCustomized(String numeroDoc, String codCliente, String codPersona);

}
