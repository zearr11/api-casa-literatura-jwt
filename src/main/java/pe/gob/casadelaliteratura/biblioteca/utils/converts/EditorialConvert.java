package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Editorial;

public class EditorialConvert {

    public static EditorialDataDto modelToDto(Editorial editorial) {
        return new EditorialDataDto(editorial.getCodEditorial(),
                editorial.getDescripcion());
    }

}
