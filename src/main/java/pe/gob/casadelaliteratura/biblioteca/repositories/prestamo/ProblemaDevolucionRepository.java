package pe.gob.casadelaliteratura.biblioteca.repositories.prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetalleDevolucion;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.ProblemaDevolucion;

import java.util.List;
import java.util.Optional;

public interface ProblemaDevolucionRepository extends JpaRepository<ProblemaDevolucion, String> {

    @Query(value = """
            SELECT pd.cod_problema_devolucion
            FROM problema_devolucion pd
            INNER JOIN detalle_devolucion dv ON dv.id_detalle_devolucion = pd.fk_cod_detalle_devolucion
            INNER JOIN devolucion dev ON dev.cod_devolucion = dv.fk_cod_devolucion
            INNER JOIN prestamo ps ON ps.cod_prestamo = dev.fk_cod_prestamo
            WHERE ps.cod_prestamo = :codPrestamo
            """, nativeQuery = true)
    Optional<List<String>> findByPrestamo(String codPrestamo);

    @Query(value = """
            SELECT COUNT(pd.cod_problema_devolucion) FROM problema_devolucion pd
            INNER JOIN detalle_devolucion dv ON dv.id_detalle_devolucion = pd.fk_cod_detalle_devolucion
            INNER JOIN devolucion dev ON dev.cod_devolucion = dv.fk_cod_devolucion
            INNER JOIN prestamo ps ON ps.cod_prestamo = dev.fk_cod_prestamo
            WHERE pd.estado_problema = 'LIBRO_REPUESTO' AND
            ps.cod_prestamo = :codPrestamo
            """, nativeQuery = true)
    Integer countLibrosRepuestos(String codPrestamo);

    Optional<ProblemaDevolucion> findByDetalleDevolucion(DetalleDevolucion detalleDevolucion);

}
