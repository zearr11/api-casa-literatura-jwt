package pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections;

public interface LibroResumenProjection {

    String getCodigo();
    String getIsbn();
    String getTitulo();
    Integer getYear();
    String getAutor();
    String getEditorial();
    String getColeccion();
    String getSala();
    Integer getCantidadCopias();
    Integer getCantidadDisponibles();
    Integer getCantidadPrestados();
    Integer getCantidadSoloLecturaEnSala();

}
