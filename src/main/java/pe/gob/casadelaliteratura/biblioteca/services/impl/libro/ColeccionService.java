package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Coleccion;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.ColeccionRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.SalaRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IColeccionService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.ColeccionConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.util.List;

@Service
public class ColeccionService implements IColeccionService {

    private final ColeccionRepository coleccionRepository;
    private final SalaRepository salaRepository;
    private final AlmacenCodigosService acService;

    public ColeccionService(ColeccionRepository coleccionRepository,
                            SalaRepository salaRepository,
                            AlmacenCodigosService acService) {
        this.coleccionRepository = coleccionRepository;
        this.salaRepository = salaRepository;
        this.acService = acService;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdate(String codColeccion,
                                           ColeccionRequestDto datosColeccion) {
        // Carga de atributos
        Coleccion coleccion;
        String msg;
        Coleccion coleccionExistente = coleccionRepository
                .findByDescripcion(datosColeccion.getColeccion())
                .orElse(null);
        Sala sala = salaRepository.findById(datosColeccion.getCodigoSala())
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró la sala con el codigo: " + datosColeccion.getCodigoSala()
                ));

        if (codColeccion == null){
            if (coleccionExistente != null)
                throw new ErrorException409("Ya existe una colección con el nombre ingresado.");

            coleccion = new Coleccion(acService.generateCodigo("CC"),
                    datosColeccion.getColeccion(), sala);
            msg = "Colección registrada satisfactoriamente.";
        }
        else {
            if (coleccionExistente != null && !coleccionExistente.getCodColeccion().equals(codColeccion))
                throw new ErrorException409("Ya existe una colección con el nombre ingresado.");

            coleccion = coleccionRepository.findById(codColeccion)
                    .orElseThrow(() ->
                            new ErrorException404(
                                    "No se encontró la colección con el codigo: " + codColeccion
                            ));

            coleccion.setDescripcion(datosColeccion.getColeccion());
            coleccion.setSala(sala);

            msg = "Colección actualizada satisfactoriamente.";
        }
        coleccionRepository.save(coleccion);

        return new MensajeDto<>(msg);
    }

    @Override
    public List<ColeccionResponseDto> getAllColecciones() {
        List<ColeccionResponseDto> resultado = coleccionRepository.findAll().stream().map(
                ColeccionConvert::coleccionToResponse
        ).toList();

        if (resultado.isEmpty())
            throw new ErrorException404("No se encontraron colecciones.");

        return resultado;
    }

    @Override
    public ColeccionResponseDto getByCodColeccion(String codColeccion) {
        Coleccion coleccion = coleccionRepository.findById(codColeccion)
                .orElseThrow(() ->
                        new ErrorException404(
                                "No se encontró la colección con el codigo: " + codColeccion
                        ));
        return ColeccionConvert.coleccionToResponse(coleccion);
    }

    @Override
    public List<ColeccionResponseDto> getBySala(Sala sala) {
        List<Coleccion> resultado = coleccionRepository.findBySala(sala)
                .orElse(null);

        return (resultado != null) ? resultado.stream()
                .map(ColeccionConvert::coleccionToResponse).toList() :
                null;
    }
}
