package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Devolucion;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.SancionDemora;

import java.util.Optional;

public interface SancionDemoraRepository extends JpaRepository<SancionDemora, String> {

    Optional<SancionDemora> findByDevolucion(Devolucion devolucion);

}
