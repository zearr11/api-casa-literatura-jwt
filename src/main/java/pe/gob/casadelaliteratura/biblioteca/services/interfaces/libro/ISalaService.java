package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto2;
import java.util.List;

public interface ISalaService {

    MensajeDto<String> saveOrUpdate(String codSala, SalaRequestDto datosSala);
    List<SalaResponseDto> getAllSalas();
    SalaResponseDto getByCodSala(String codSala);
    List<SalaResponseDto2> getAllWithColecciones();
    SalaResponseDto2 getByCodWithColecciones(String codSala);

}
