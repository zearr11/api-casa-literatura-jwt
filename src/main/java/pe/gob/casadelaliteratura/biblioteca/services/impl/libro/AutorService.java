package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Autor;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.AutorRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.projections.AutorProjection;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IAutorService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.AutorConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.util.List;

@Service
public class AutorService implements IAutorService {

    private final AutorRepository autorRepository;
    private final AlmacenCodigosService acService;

    public AutorService(AutorRepository autorRepository, AlmacenCodigosService acService) {
        this.autorRepository = autorRepository;
        this.acService = acService;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdate(String codAutor,
                                           AutorRequestDto datosAutor) {
        // Carga de atributos
        Autor autor;
        String msg;
        AutorProjection autorExistente = autorRepository
                .findByNameAndNacionality(datosAutor.getNombre(),
                        datosAutor.getNacionalidad()).orElse(null);

        if (codAutor == null) { // NUEVO AUTOR
            if (autorExistente != null)
                throw new ErrorException409("Ya existe un autor con los datos ingresados.");

            autor = new Autor(acService.generateCodigo("AT"),
                    datosAutor.getNombre(), datosAutor.getNacionalidad());
            acService.updateTable("AT");
            msg = "Autor registrado satisfactoriamente.";
        }
        else { // EDITAR AUTOR
            if (autorExistente != null && !autorExistente.getCodAutor().equals(codAutor))
                throw new ErrorException409("Ya existe un autor con los datos ingresados.");

            autor = autorRepository.findById(codAutor)
                    .orElseThrow(() ->
                            new ErrorException404(
                                    "No se encontró al autor con el codigo: " + codAutor
                            ));

            autor.setNombre(datosAutor.getNombre());
            autor.setNacionalidad(datosAutor.getNacionalidad());

            msg = "Autor actualizado satisfactoriamente.";
        }
        autorRepository.save(autor);

        return new MensajeDto<>(msg);

    }

    @Override
    public List<AutorResponseDto> getAllAutores() {
        List<AutorResponseDto> resultado = autorRepository.findAll().stream().map(
                AutorConvert::autorToResponse
        ).toList();

        if (resultado.isEmpty())
            throw new ErrorException404("No se encontraron autores.");

        return resultado;
    }

    @Override
    public AutorResponseDto getByCodAutor(String codAutor) {
        Autor autor = autorRepository.findById(codAutor)
                .orElseThrow(() ->
                        new ErrorException404(
                                "No se encontró al autor con el codigo: " + codAutor
                        ));

        return AutorConvert.autorToResponse(autor);
    }
}
