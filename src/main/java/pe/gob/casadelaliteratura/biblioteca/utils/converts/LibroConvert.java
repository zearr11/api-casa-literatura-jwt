package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.LibroDetalleResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.copias.LibroCopiaResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.copias.LibroCopiaResponseDto2;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.LibroCopiasProjection;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.LibroResumenProjection;

public class LibroConvert {

    public static LibroDetalleResponseDto libroResumenProjectionToLibroDetalleResponseDto(
            LibroResumenProjection libro) {

        return new LibroDetalleResponseDto(
                libro.getCodigo(),
                libro.getIsbn(), libro.getTitulo(), libro.getYear(), libro.getAutor(), libro.getEditorial(),
                libro.getColeccion(), libro.getSala(), libro.getCantidadCopias(), libro.getCantidadDisponibles(),
                libro.getCantidadPrestados(), libro.getCantidadSoloLecturaEnSala()
        );

    }

    public static LibroCopiaResponseDto libroCopiasProjectionToLibroCopiaResponseDto
            (LibroCopiasProjection libro) {

        return new LibroCopiaResponseDto(libro.getCodigo(),
                libro.getIsbn(), libro.getTitulo(), libro.getYear(), libro.getAutor(),
                libro.getEditorial(), libro.getNumeroCopia(), libro.getColeccion(),
                libro.getSala(), libro.getEstado(), libro.getFechaVencimiento()
        );

    }

    public static LibroCopiaResponseDto2 libroCopiasProjectionToLibroCopiaResponseDto2
            (LibroCopiasProjection libro) {

        return new LibroCopiaResponseDto2(libro.getCodigo(),
                libro.getIsbn(), libro.getTitulo(), libro.getYear(), libro.getAutor(),
                libro.getEditorial(), libro.getNumeroCopia(), libro.getColeccion(),
                libro.getSala(), libro.getEstado()
        );

    }

}
