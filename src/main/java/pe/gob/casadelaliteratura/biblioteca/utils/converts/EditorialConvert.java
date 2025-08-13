package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Editorial;

public class EditorialConvert {

    public static EditorialResponseDto editorialToResponse(Editorial editorial) {
        return new EditorialResponseDto(editorial.getCodEditorial(),
                editorial.getDescripcion());
    }

}
