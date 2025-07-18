package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataSimpleDto;

import java.util.List;

public interface IAutorService {
    List<AutorDataDto> getAll();
    AutorDataDto getByCod(String codAutor);
    MensajeDto<String> saveOrUpdate(String codAutor, AutorDataSimpleDto datosAutor);
}
