package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialResponseDto;
import java.util.List;

public interface IEditorialService {

    MensajeDto<String> saveOrUpdate(String codEditorial, EditorialRequestDto datosEditorial);
    List<EditorialResponseDto> getAllEditoriales();
    EditorialResponseDto getByCodEditorial(String codEditorial);

}
