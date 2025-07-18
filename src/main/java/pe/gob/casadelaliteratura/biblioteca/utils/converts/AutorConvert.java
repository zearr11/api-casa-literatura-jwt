package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Autor;

public class AutorConvert {

    public static AutorDataDto modelToDto(Autor autor) {
        return new AutorDataDto(autor.getCodAutor(),
                autor.getNombre(), autor.getNacionalidad());
    }

}
