package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.SolucionDevolucion;

public interface SolucionDevolucionRepository extends JpaRepository<SolucionDevolucion, String> {
}
