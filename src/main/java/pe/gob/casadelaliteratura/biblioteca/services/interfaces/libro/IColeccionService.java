package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataSimpleDto;

import java.util.List;

public interface IColeccionService {
    List<ColeccionDataDto> getAll();
    ColeccionDataDto getByCod(String codColeccion);
    MensajeDto<String> saveOrUpdate(String codColeccion, ColeccionDataSimpleDto datosColeccion);
}
