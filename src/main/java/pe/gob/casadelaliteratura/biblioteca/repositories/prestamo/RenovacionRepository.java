package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Prestamo;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Renovacion;
import java.util.List;

public interface RenovacionRepository extends JpaRepository<Renovacion, Long> {

    List<Renovacion> findByPrestamo(Prestamo prestamo);

}
