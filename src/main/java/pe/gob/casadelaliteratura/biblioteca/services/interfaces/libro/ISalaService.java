package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataSimpleDto;

import java.util.List;

public interface ISalaService {
    List<SalaDataDto>  getAll();
    SalaDataDto getByCod(String codSala);
    List<SalaData2Dto> getAllWithColecciones();
    SalaData2Dto getByCodWithColecciones(String codSala);
    MensajeDto<String> saveOrUpdate(String codSala, SalaDataSimpleDto datosSala);
}
