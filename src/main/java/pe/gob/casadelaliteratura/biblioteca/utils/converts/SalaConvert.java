package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;

public class SalaConvert {

    public static SalaResponseDto salaToResponse(Sala sala) {
        return new SalaResponseDto(sala.getCodSala(),
                sala.getNombreSala());
    }

    public static Sala responseToSala(SalaResponseDto sala) {
        return new Sala(sala.getCodigo(), sala.getSala());
    }

}
