package pe.gob.casadelaliteratura.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.AlmacenCodigos;

public interface AlmacenCodigosRepository extends JpaRepository<AlmacenCodigos, String> {}
