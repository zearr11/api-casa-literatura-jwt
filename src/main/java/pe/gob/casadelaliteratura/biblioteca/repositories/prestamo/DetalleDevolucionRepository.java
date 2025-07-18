package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetalleDevolucion;

public interface DetalleDevolucionRepository extends JpaRepository<DetalleDevolucion, Long> {
}
