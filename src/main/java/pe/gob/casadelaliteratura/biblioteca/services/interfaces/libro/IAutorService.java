package pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorResponseDto;
import java.util.List;

public interface IAutorService {

    MensajeDto<String> saveOrUpdate(String codAutor, AutorRequestDto datosAutor);
    List<AutorResponseDto> getAllAutores();
    AutorResponseDto getByCodAutor(String codAutor);

}
