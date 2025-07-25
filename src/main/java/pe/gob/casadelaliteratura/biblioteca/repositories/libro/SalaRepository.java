package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, String> {

    Optional<Sala> findByNombreSala(String nombreSala);

}
