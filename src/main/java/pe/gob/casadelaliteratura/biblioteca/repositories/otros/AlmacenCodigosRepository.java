package pe.gob.casadelaliteratura.biblioteca.repositories.otros;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.otros.AlmacenCodigos;

public interface AlmacenCodigosRepository extends JpaRepository<AlmacenCodigos, String> {

}
