package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Coleccion;

public class ColeccionConvert {

    public static ColeccionResponseDto coleccionToResponse(Coleccion coleccion) {
        return new ColeccionResponseDto(coleccion.getCodColeccion(),
                coleccion.getDescripcion());
    }

}
