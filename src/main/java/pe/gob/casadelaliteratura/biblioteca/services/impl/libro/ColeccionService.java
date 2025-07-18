package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Coleccion;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.ColeccionRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IColeccionService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ISalaService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.ColeccionConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.SalaConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class ColeccionService implements IColeccionService {

    private final ColeccionRepository repository;
    private final ISalaService salaService;
    private final AlmacenCodigosService acService;

    public ColeccionService(ColeccionRepository repository,
                            ISalaService salaService,
                            AlmacenCodigosService acService) {
        this.repository = repository;
        this.salaService = salaService;
        this.acService = acService;
    }

    @Override
    public List<ColeccionDataDto> getAll() {
        List<ColeccionDataDto> resultado = repository.findAll().stream().map(
                ColeccionConvert::modelToDto
        ).toList();

        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron colecciones.");

        return resultado;
    }

    @Override
    public ColeccionDataDto getByCod(String codColeccion) {
        Coleccion coleccion = repository.findById(codColeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Colección con codigo " +
                        codColeccion + " no existe."));

        return ColeccionConvert.modelToDto(coleccion);
    }

    @Override
    public MensajeDto<String> saveOrUpdate(String codColeccion,
                                           ColeccionDataSimpleDto datosColeccion) {

        Coleccion coleccion;
        String msg;
        Coleccion coleccionExistente = repository.findByDescripcion(datosColeccion.getNombreColeccion())
                .orElse(null);
        Sala sala = SalaConvert
                .dtoToModel(salaService.getByCod(datosColeccion.getCodigoSala()));

        if (codColeccion == null){
            if (coleccionExistente != null)
                throw new ResourceNotFoundException("Ya existe una coleccion con el nombre ingresado.");

            coleccion = new Coleccion(acService.generateCodigo("CC"),
                    datosColeccion.getNombreColeccion(), sala);
            acService.updateTable("CC");

            msg = "Coleccion registrada satisfactoriamente.";
        }
        else {
            if (coleccionExistente != null && !coleccionExistente.getCodColeccion().equals(codColeccion))
                throw new ResourceNotFoundException("Ya existe una coleccion con el nombre ingresado.");

            coleccion = repository.findById(codColeccion)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Coleccion con codigo " +
                                    codColeccion + " no existe."));

            coleccion.setDescripcion(datosColeccion.getNombreColeccion());
            coleccion.setSala(sala);

            msg = "Coleccion actualizada satisfactoriamente.";
        }
        repository.save(coleccion);

        return new MensajeDto<>(msg);
    }

}
