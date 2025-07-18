package pe.gob.casadelaliteratura.biblioteca.repositories.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Documentacion;

public interface DocumentacionRepository extends JpaRepository<Documentacion, Long> {
}
