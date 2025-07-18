package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Editorial;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.EditorialRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IEditorialService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.EditorialConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class EditorialService implements IEditorialService {

    private final EditorialRepository repository;
    private final AlmacenCodigosService acService;

    public EditorialService(EditorialRepository repository,
                            AlmacenCodigosService acService) {
        this.repository = repository;
        this.acService = acService;
    }

    @Override
    public List<EditorialDataDto> getAll() {
        List<EditorialDataDto> resultado = repository.findAll().stream().map(
                EditorialConvert::modelToDto
        ).toList();

        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron editoriales.");

        return resultado;
    }

    @Override
    public EditorialDataDto getByCod(String codEditorial) {
        Editorial editorial = repository.findById(codEditorial)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Editorial con codigo " +
                                codEditorial + " no existe."));

        return EditorialConvert.modelToDto(editorial);
    }

    @Override
    public MensajeDto<String> saveOrUpdate(String codEditorial, EditorialDataSimpleDto datosEditorial) {

        Editorial editorial;
        String msg;
        Editorial editorialExistente = repository
                .findByDescripcion(datosEditorial.getNombreEditorial())
                .orElse(null);

        if (codEditorial == null){
            if (editorialExistente != null)
                throw new ResourceNotFoundException("Ya existe una editorial con el nombre ingresado.");

            editorial = new Editorial(acService.generateCodigo("ED"),
                    datosEditorial.getNombreEditorial());
            acService.updateTable("ED");

            msg = "Editorial registrada satisfactoriamente.";
        }
        else {
            if (editorialExistente != null && !editorialExistente.getCodEditorial().equals(codEditorial))
                throw new ResourceNotFoundException("Ya existe una editorial con el nombre ingresado.");

            editorial = repository.findById(codEditorial)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Editorial con codigo " + codEditorial + " no existe."));

            editorial.setDescripcion(datosEditorial.getNombreEditorial());
            msg = "Editorial actualizada satisfactoriamente.";
        }
        repository.save(editorial);

        return new MensajeDto<>(msg);
    }

}
