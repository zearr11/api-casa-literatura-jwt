package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Autor;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.AutorProjection;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, String> {

    @Query(value = """
            SELECT cod_autor, nacionalidad, nombre FROM autor
            WHERE nombre = :nombre AND nacionalidad = :nacionalidad
            """, nativeQuery = true)
    Optional<AutorProjection> findByNameAndNacionality(String nombre, String nacionalidad);

}
