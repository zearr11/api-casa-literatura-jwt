package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Autor;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, String> {
    @Query(value = """
            SELECT cod_autor FROM autor
            WHERE nombre = :nombre AND nacionalidad = :nacionalidad
            """, nativeQuery = true)
    Optional<String> findByNameAndNacionality(String nombre, String nacionalidad);
}
