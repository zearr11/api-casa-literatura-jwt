package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.SalaColeccionProjection;

import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, String> {

    Optional<Sala> findByNombreSala(String nombreSala);

    @Query(value = """
            SELECT s.cod_sala, s.nombre_sala,
            c.cod_coleccion, c.descripcion
            FROM sala s
            INNER JOIN coleccion c ON s.cod_sala = c.id_sala
            ORDER BY s.cod_sala
            """, nativeQuery = true)
    List<SalaColeccionProjection> salaWithColecciones();

    @Query(value = """
            SELECT s.cod_sala, s.nombre_sala,
            c.cod_coleccion, c.descripcion
            FROM sala s
            INNER JOIN coleccion c ON s.cod_sala = c.id_sala
            WHERE s.cod_sala = :codSala
            ORDER BY s.cod_sala
            """, nativeQuery = true)
    List<SalaColeccionProjection> salaWithColeccionesById(String codSala);

}
