package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetalleDevolucion;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetallePrestamo;

import java.util.Optional;

public interface DetalleDevolucionRepository extends JpaRepository<DetalleDevolucion, Long> {

    Optional<DetalleDevolucion> findByDetallePrestamo(DetallePrestamo detallePrestamo);

}
