package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Autor;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.AutorRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IAutorService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.AutorConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public class AutorService implements IAutorService {

    private final AutorRepository repository;
    private final AlmacenCodigosService acService;

    public AutorService(AutorRepository repository, AlmacenCodigosService acService) {
        this.repository = repository;
        this.acService = acService;
    }

    @Override
    public List<AutorDataDto> getAll() {
        List<AutorDataDto> resultado = repository.findAll().stream().map(
                AutorConvert::modelToDto
        ).toList();

        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron autores.");

        return resultado;
    }

    @Override
    public AutorDataDto getByCod(String codAutor) {
        Autor autor = repository.findById(codAutor)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Autor con codigo " + codAutor + " no existe."));

        return AutorConvert.modelToDto(autor);
    }

    @Override
    public MensajeDto<String> saveOrUpdate(String codAutor, AutorDataSimpleDto datosAutor) {

        Autor autor;
        String msg;

        String codAutorExistente = repository
                .findByNameAndNacionality(datosAutor.getNombre(),
                        datosAutor.getNacionalidad()).orElse(null);

        if (codAutor == null){
            if (codAutorExistente != null)
                throw new ResourceNotFoundException("Ya existe un autor con los datos ingresados.");

            autor = new Autor(acService.generateCodigo("AT"),
                    datosAutor.getNombre(), datosAutor.getNacionalidad());
            acService.updateTable("AT");

            msg = "Autor registrado satisfactoriamente.";
        }
        else {
            if (codAutorExistente != null && !codAutorExistente.equals(codAutor))
                throw new ResourceNotFoundException("Ya existe un autor con los datos ingresados.");

            autor = repository.findById(codAutor)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Autor con codigo " + codAutor + " no existe."));

            autor.setNombre(datosAutor.getNombre());
            autor.setNacionalidad(datosAutor.getNacionalidad());

            msg = "Autor actualizado satisfactoriamente.";
        }
        repository.save(autor);

        return new MensajeDto<>(msg);
    }
}
