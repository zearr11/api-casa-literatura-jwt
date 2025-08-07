package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Editorial;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.EditorialRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IEditorialService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.EditorialConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.util.List;

@Service
public class EditorialService implements IEditorialService {

    private final EditorialRepository editorialRepository;
    private final AlmacenCodigosService acService;

    public EditorialService(EditorialRepository editorialRepository, AlmacenCodigosService acService) {
        this.editorialRepository = editorialRepository;
        this.acService = acService;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdate(String codEditorial,
                                           EditorialRequestDto datosEditorial) {
        // Carga de atributos
        Editorial editorial;
        String msg;
        Editorial editorialExistente = editorialRepository
                .findByDescripcion(datosEditorial.getEditorial())
                .orElse(null);

        if (codEditorial == null){ // EDITORIAL NUEVO
            if (editorialExistente != null)
                throw new ErrorException409("Ya existe una editorial con el nombre ingresado.");

            editorial = new Editorial(acService.generateCodigo("ED"),
                                        datosEditorial.getEditorial());
            acService.updateTable("ED");

            msg = "Editorial registrada satisfactoriamente.";
        }
        else { // EDITAR EDITORIAL
            if (editorialExistente != null && !editorialExistente.getCodEditorial().equals(codEditorial))
                throw new ErrorException409("Ya existe una editorial con el nombre ingresado.");

            editorial = editorialRepository.findById(codEditorial)
                    .orElseThrow(() ->
                            new ErrorException404(
                                    "No se encontró la editorial con el codigo: " + codEditorial
                            ));
            editorial.setDescripcion(datosEditorial.getEditorial());

            msg = "Editorial actualizada satisfactoriamente.";
        }
        editorialRepository.save(editorial);

        return new MensajeDto<>(msg);
    }

    @Override
    public List<EditorialResponseDto> getAllEditoriales() {
        List<EditorialResponseDto> resultado = editorialRepository.findAll().stream().map(
                EditorialConvert::editorialToResponse
        ).toList();

        if (resultado.isEmpty())
            throw new ErrorException404("No se encontraron editoriales.");

        return resultado;
    }

    @Override
    public EditorialResponseDto getByCodEditorial(String codEditorial) {
        Editorial editorial = editorialRepository.findById(codEditorial)
                .orElseThrow(() ->
                        new ErrorException404(
                                "No se encontró la editorial con el codigo: " + codEditorial
                        ));
        return EditorialConvert.editorialToResponse(editorial);
    }

}
