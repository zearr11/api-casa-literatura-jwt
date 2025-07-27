package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Devolucion;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Prestamo;

import java.util.Optional;

public interface DevolucionRepository extends JpaRepository<Devolucion, String> {

    Optional<Devolucion> findByPrestamo(Prestamo prestamo);

}
