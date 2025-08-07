package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.ProblemaDevolucion;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.SolucionDevolucion;
import java.util.Optional;

public interface SolucionDevolucionRepository extends JpaRepository<SolucionDevolucion, String> {

    Optional<SolucionDevolucion> findByProblemaDevolucion(ProblemaDevolucion problemaDevolucion);

    @Query(value = """
            SELECT COUNT(sd.cod_solucion_dev)
            FROM solucion_devolucion sd
            INNER JOIN problema_devolucion pd ON pd.cod_problema_devolucion = sd.fk_cod_problema_devolucion
            INNER JOIN detalle_devolucion dv ON dv.id_detalle_devolucion = pd.fk_cod_detalle_devolucion
            INNER JOIN devolucion dev ON dev.cod_devolucion = dv.fk_cod_devolucion
            INNER JOIN prestamo ps ON ps.cod_prestamo = dev.fk_cod_prestamo
            WHERE ps.cod_prestamo = :codPrestamo
            """, nativeQuery = true)
    Integer countByPrestamo(String codPrestamo);

}
