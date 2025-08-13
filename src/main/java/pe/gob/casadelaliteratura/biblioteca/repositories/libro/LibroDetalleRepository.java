package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.libro.LibroDetalle;

import java.util.Optional;

public interface LibroDetalleRepository extends JpaRepository<LibroDetalle, String> {

    Optional<LibroDetalle> findByIsbn(String isbn);

}
