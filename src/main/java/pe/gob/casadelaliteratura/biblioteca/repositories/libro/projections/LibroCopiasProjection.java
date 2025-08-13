package pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections;

import java.time.LocalDate;

public interface LibroCopiasProjection {
    String getCodigo();
    String getIsbn();
    String getTitulo();
    Integer getYear();
    String getAutor();
    String getEditorial();
    Integer getNumeroCopia();
    String getColeccion();
    String getSala();
    String getEstado();
    LocalDate getFechaVencimiento();
}
