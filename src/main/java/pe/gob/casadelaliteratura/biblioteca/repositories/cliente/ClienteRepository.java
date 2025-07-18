package pe.gob.casadelaliteratura.biblioteca.repositories.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Cliente;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query(value = """
            SELECT cod_cliente FROM cliente
            WHERE numero_doc = :numeroDoc
            """, nativeQuery = true)
    Optional<String> validateDniAndNumDoc(String numeroDoc);

    Optional<Cliente> findByNumeroDoc(String numeroDoc);

}
