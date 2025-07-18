package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataSimpleDto;

import java.util.List;

public interface IEditorialService {
    List<EditorialDataDto> getAll();
    EditorialDataDto getByCod(String codEditorial);
    MensajeDto<String> saveOrUpdate(String codEditorial, EditorialDataSimpleDto datosEditorial);
}
