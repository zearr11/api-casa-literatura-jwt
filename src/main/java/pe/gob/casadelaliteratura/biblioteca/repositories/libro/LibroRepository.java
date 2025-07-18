package pe.gob.casadelaliteratura.biblioteca.repositories.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Libro;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.LibroCopiasProjection;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.LibroResumenProjection;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query(value = """
        SELECT ld.cod_libro_detalle as codigo, ld.isbn, ld.titulo, ld.year, a.nombre AS autor,
               e.descripcion AS editorial, c.descripcion AS coleccion,
               s.nombre_sala AS sala, COUNT(l.id_libro) AS cantidad_copias,
               SUM(CASE WHEN l.estado = 'DISPONIBLE' THEN 1 ELSE 0 END) AS cantidad_disponibles,
               SUM(CASE WHEN l.estado = 'PRESTADO' THEN 1 ELSE 0 END) AS cantidad_prestados,
               SUM(CASE WHEN l.estado = 'SOLO_PARA_LECTURA_EN_SALA' THEN 1 ELSE 0 END) AS cantidad_solo_lectura_en_sala
        FROM libro_detalle ld
        LEFT JOIN libro l ON l.id_libro_detalle = ld.cod_libro_detalle
        INNER JOIN autor a ON a.cod_autor = ld.id_autor
        INNER JOIN editorial e ON e.cod_editorial = ld.id_editorial
        INNER JOIN coleccion c ON c.cod_coleccion = ld.id_coleccion
        INNER JOIN sala s ON s.cod_sala = c.id_sala
        WHERE (:codigo IS NULL OR ld.cod_libro_detalle = :codigo)
          AND(:titulo IS NULL OR ld.titulo LIKE %:titulo%)
          AND (:isbn IS NULL OR ld.isbn LIKE %:isbn%)
          AND (:year IS NULL OR ld.year = :year)
          AND (:autor IS NULL OR a.nombre LIKE %:autor%)
          AND (:editorial IS NULL OR e.descripcion LIKE %:editorial%)
          AND (:coleccion IS NULL OR c.descripcion LIKE %:coleccion%)
          AND (:sala IS NULL OR s.nombre_sala LIKE %:sala%)
        GROUP BY ld.cod_libro_detalle, ld.isbn, ld.titulo, ld.year, a.nombre, e.descripcion,
                 c.descripcion, s.nombre_sala
        HAVING (:cantidadCopias IS NULL OR COUNT(*) = :cantidadCopias)
           AND (:cantidadDisponibles IS NULL OR SUM(CASE WHEN l.estado = 'DISPONIBLE' THEN 1 ELSE 0 END) = :cantidadDisponibles)
           AND (:cantidadPrestados IS NULL OR SUM(CASE WHEN l.estado = 'PRESTADO' THEN 1 ELSE 0 END) = :cantidadPrestados)
           AND (:cantidadSoloLectura IS NULL OR SUM(CASE WHEN l.estado = 'SOLO_PARA_LECTURA_EN_SALA' THEN 1 ELSE 0 END) = :cantidadSoloLectura)
        ORDER BY titulo
        """, nativeQuery = true)
    List<LibroResumenProjection> obtenerResumenLibros(
            String codigo, String titulo, String isbn, Integer year,
            String autor, String editorial, String coleccion, String sala,
            Integer cantidadCopias, Integer cantidadDisponibles,
            Integer cantidadPrestados, Integer cantidadSoloLectura
    );

    @Query(value = """
        SELECT ld.cod_libro_detalle as codigo, ld.isbn, ld.titulo, ld.year, a.nombre AS autor,
               e.descripcion AS editorial, l.numero_copia, c.descripcion AS coleccion,
               s.nombre_sala AS sala, l.estado,
               CASE
                   WHEN l.estado = 'PRESTADO'
                   THEN COALESCE(r.nueva_fecha_vencimiento, p.fecha_vencimiento)
                   ELSE NULL
               END AS fecha_vencimiento
        FROM libro_detalle ld
        INNER JOIN libro l ON l.id_libro_detalle = ld.cod_libro_detalle
        INNER JOIN autor a ON a.cod_autor = ld.id_autor
        INNER JOIN editorial e ON e.cod_editorial = ld.id_editorial
        INNER JOIN coleccion c ON c.cod_coleccion = ld.id_coleccion
        INNER JOIN sala s ON s.cod_sala = c.id_sala
        LEFT JOIN detalle_prestamo dp ON dp.id_libro = l.id_libro
        LEFT JOIN prestamo p ON p.cod_prestamo = dp.id_prestamo
        LEFT JOIN (
            SELECT r1.id_prestamo, r1.nueva_fecha_vencimiento
            FROM renovacion r1
            INNER JOIN (
                SELECT id_prestamo, MAX(fecha_solicitud) AS max_fecha
                FROM renovacion
                GROUP BY id_prestamo
            ) r2 ON r1.id_prestamo = r2.id_prestamo AND r1.fecha_solicitud = r2.max_fecha
        ) r ON r.id_prestamo = p.cod_prestamo
        WHERE (:codigo IS NULL OR ld.cod_libro_detalle = :codigo)
          AND (:isbn IS NULL OR ld.isbn LIKE %:isbn%)
          AND (:titulo IS NULL OR ld.titulo LIKE %:titulo%)
          AND (:year IS NULL OR ld.year = :year)
          AND (:autor IS NULL OR a.nombre LIKE %:autor%)
          AND (:editorial IS NULL OR e.descripcion LIKE %:editorial%)
          AND (:numeroCopia IS NULL OR l.numero_copia = :numeroCopia)
          AND (:coleccion IS NULL OR c.descripcion LIKE %:coleccion%)
          AND (:sala IS NULL OR s.nombre_sala LIKE %:sala%)
          AND (:estado IS NULL OR l.estado = :estado)
          ORDER BY titulo
        """, nativeQuery = true)
    List<LibroCopiasProjection> obtenerCopiasLibros(String codigo, String isbn, String titulo,
                                                    Integer year, String autor, String editorial,
                                                    Integer numeroCopia, String coleccion, String sala,
                                                    String estado);

    @Query(value = """
            SELECT COALESCE(MAX(numero_copia), 0) AS n_copia_mayor
            FROM libro
            WHERE id_libro_detalle = :codigoLibDet;
           """, nativeQuery = true)
    Integer getNumeroCopiaMayor(String codigoLibDet);

    @Query(value = """
            SELECT id_libro FROM libro
            WHERE id_libro_detalle = :codLibro AND
            numero_copia = :numCopia
            """, nativeQuery = true)
    Long findLibroByCodAndNumCopia(String codLibro, Integer numCopia);

}
