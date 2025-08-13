package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Autor;

public class AutorConvert {

    public static AutorResponseDto autorToResponse(Autor autor) {
        return new AutorResponseDto(autor.getCodAutor(),
                autor.getNombre(), autor.getNacionalidad());
    }

}
