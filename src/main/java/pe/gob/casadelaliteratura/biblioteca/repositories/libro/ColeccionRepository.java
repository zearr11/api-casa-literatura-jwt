package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Coleccion;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;

import java.util.List;
import java.util.Optional;

public interface ColeccionRepository extends JpaRepository<Coleccion, String> {

    Optional<Coleccion> findByDescripcion(String nombreColeccion);
    Optional<List<Coleccion>> findBySala(Sala sala);

}
