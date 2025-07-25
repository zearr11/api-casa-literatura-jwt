package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request.LibroDetalleNuevoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request.LibroNuevaCopiaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.copias.ILibroCopiaResponse;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.LibroDetalleResponseDto;
import java.util.List;

public interface ILibroService {

    MensajeDto<String> saveLibroCopia(LibroNuevaCopiaRequestDto datosLibro);
    MensajeDto<String> saveOrUpdateLibroDetalle(String codigo, LibroDetalleNuevoRequestDto datosLibro);

    List<LibroDetalleResponseDto> getAllResumen(String codigo, String titulo, String isbn, Integer year, String autor,
                                                String editorial, String coleccion, String sala,
                                                Integer cantidadCopias, Integer cantidadDisponibles,
                                                Integer cantidadPrestados, Integer cantidadSoloLectura);

    List<ILibroCopiaResponse> getAllCopias(String codigo, String isbn, String titulo,
                                           Integer year, String autor,
                                           String editorial, String coleccion, String sala,
                                           Integer numeroCopia, String estado);

}
