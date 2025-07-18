package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Coleccion;

public class ColeccionConvert {

    public static ColeccionDataDto modelToDto(Coleccion coleccion) {
        return new ColeccionDataDto(coleccion.getCodColeccion(),
                coleccion.getDescripcion());
    }

}
