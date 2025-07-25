package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Editorial;

import java.util.Optional;

public interface EditorialRepository extends JpaRepository<Editorial, String> {

    Optional<Editorial> findByDescripcion(String nombreEditorial);

}
