package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Renovacion;

public interface RenovacionRepository extends JpaRepository<Renovacion, String> {
}
