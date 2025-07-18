package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.SalaColeccionProjection;
import java.util.ArrayList;
import java.util.List;

public class SalaConvert {

    public static SalaDataDto modelToDto(Sala sala) {
        return new SalaDataDto(sala.getCodSala(),
                sala.getNombreSala());
    }

    public static Sala dtoToModel(SalaDataDto sala) {
        return new Sala(sala.getCodigo(),
                sala.getNombreSala());
    }

    public static List<SalaData2Dto> projectionToListDto2(List<SalaColeccionProjection> salas) {
        List<SalaData2Dto> salasDto = new ArrayList<>();
        List<ColeccionDataDto> coleccionData = new ArrayList<>();

        if (salas.isEmpty()) return salasDto;

        String codSalaActual = salas.getFirst().getCodSala();
        String nombreSalaActual = salas.getFirst().getNombreSala();

        for (int i = 0; i < salas.size(); i++) {
            SalaColeccionProjection current = salas.get(i);

            if (codSalaActual.equals(current.getCodSala())) {
                coleccionData.add(new ColeccionDataDto(current.getCodColeccion(),
                        current.getDescripcion()));
            } else {
                salasDto.add(new SalaData2Dto(codSalaActual, nombreSalaActual,
                        new ArrayList<>(coleccionData)));

                codSalaActual = current.getCodSala();
                nombreSalaActual = current.getNombreSala();
                coleccionData.clear();
                coleccionData.add(new ColeccionDataDto(current.getCodColeccion(),
                        current.getDescripcion()));
            }
        }
        salasDto.add(new SalaData2Dto(codSalaActual, nombreSalaActual, new ArrayList<>(coleccionData)));

        return salasDto;
    }

    public static SalaData2Dto projectionToDto2(List<SalaColeccionProjection> salas) {
        if (salas.isEmpty()) return null;

        String codSala = salas.getFirst().getCodSala();
        String nombreSala = salas.getFirst().getNombreSala();

        List<ColeccionDataDto> colecciones = new ArrayList<>();

        for (SalaColeccionProjection current : salas) {
            colecciones.add(new ColeccionDataDto(
                    current.getCodColeccion(), current.getDescripcion()
            ));
        }

        return new SalaData2Dto(codSala, nombreSala, colecciones);
    }

}
