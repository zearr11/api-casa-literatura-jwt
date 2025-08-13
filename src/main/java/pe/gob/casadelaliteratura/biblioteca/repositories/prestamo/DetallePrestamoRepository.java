package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetallePrestamo;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Prestamo;
import java.util.List;

public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Long> {

    List<DetallePrestamo> findByPrestamo(Prestamo prestamo);

}
