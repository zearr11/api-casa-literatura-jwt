package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;

import java.util.List;

public interface IColeccionService {

    MensajeDto<String> saveOrUpdate(String codColeccion, ColeccionRequestDto datosColeccion);
    List<ColeccionResponseDto> getAllColecciones();
    ColeccionResponseDto getByCodColeccion(String codColeccion);
    List<ColeccionResponseDto> getBySala(Sala sala);

}
