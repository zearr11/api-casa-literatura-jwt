package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetallePrestamo;

public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Long> {
}
