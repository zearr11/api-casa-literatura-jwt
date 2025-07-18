package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.ILibroCopiaDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.LibroDetalleDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.request.LibroCopiaNuevoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.request.LibroDetalleNuevoDto;
import java.util.List;

public interface ILibroService {
    List<LibroDetalleDataDto> getAllResumen(String codigo, String titulo, String isbn, Integer year, String autor,
                                            String editorial, String coleccion, String sala,
                                            Integer cantidadCopias, Integer cantidadDisponibles,
                                            Integer cantidadPrestados, Integer cantidadSoloLectura);

    List<ILibroCopiaDataDto> getAllCopias(String codigo, String isbn, String titulo,
                                          Integer year, String autor,
                                          String editorial, String coleccion, String sala,
                                          Integer numeroCopia, String estado);

    MensajeDto<String> saveOrUpdateLibroDetalle(String codigo, LibroDetalleNuevoDto datosLibro);

    MensajeDto<String> saveLibroCopia(LibroCopiaNuevoDto datosLibro);
}
