package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto2;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.SalaRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IColeccionService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ISalaService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.SalaConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaService implements ISalaService {

    private final SalaRepository salaRepository;
    private final IColeccionService coleccionService;
    private final AlmacenCodigosService acService;

    public SalaService(SalaRepository salaRepository,
                       IColeccionService coleccionService,
                       AlmacenCodigosService acService) {
        this.salaRepository = salaRepository;
        this.coleccionService = coleccionService;
        this.acService = acService;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdate(String codSala,
                                           SalaRequestDto datosSala) {
        // Carga de atributos
        Sala sala;
        String msg;
        Sala salaExistente = salaRepository.findByNombreSala(datosSala.getSala())
                .orElse(null);

        if (codSala == null){
            if (salaExistente != null)
                throw new ErrorException409("Ya existe una sala con el nombre ingresado.");

            sala = new Sala(acService.generateCodigo("SL"), datosSala.getSala());
            acService.updateTable("SL");

            msg = "Sala registrada satisfactoriamente.";
        }
        else {
            if (salaExistente != null && !salaExistente.getCodSala().equals(codSala))
                throw new ErrorException409("Ya existe una sala con el nombre ingresado.");

            sala = salaRepository.findById(codSala)
                    .orElseThrow(() ->
                            new ErrorException404(
                                    "No se encontró la sala con el codigo: " + codSala
                            ));
            sala.setNombreSala(datosSala.getSala());

            msg = "Sala actualizada satisfactoriamente.";
        }
        salaRepository.save(sala);

        return new MensajeDto<>(msg);
    }

    @Override
    public List<SalaResponseDto> getAllSalas() {
        List<SalaResponseDto> resultado = salaRepository.findAll().stream().map(
                SalaConvert::salaToResponse
        ).toList();

        if (resultado.isEmpty())
            throw new ErrorException404("No se encontraron salas.");

        return resultado;
    }

    @Override
    public SalaResponseDto getByCodSala(String codSala) {
        Sala sala = salaRepository.findById(codSala)
                .orElseThrow(() ->
                        new ErrorException404(
                                "No se encontró la sala con el codigo: " + codSala
                        ));
        return SalaConvert.salaToResponse(sala);
    }

    @Override
    public List<SalaResponseDto2> getAllWithColecciones() {

        List<SalaResponseDto2> resultado = new ArrayList<>();
        List<Sala> salas = salaRepository.findAll();

        if (salas.isEmpty())
            throw new ErrorException404("No se encontraron salas.");

        for (Sala sala : salas) {
            List<ColeccionResponseDto> dataColeccion = coleccionService.getBySala(sala);
            if (dataColeccion != null) {
                SalaResponseDto2 data = new SalaResponseDto2(
                        sala.getCodSala(), sala.getNombreSala(), dataColeccion);
                resultado.add(data);
            }
        }

        if (resultado.isEmpty())
            throw new ErrorException404(
                    "Las salas registradas aún no están asociadas a ninguna colección."
            );

        return resultado;
    }

    @Override
    public SalaResponseDto2 getByCodWithColecciones(String codSala) {

        Sala sala = salaRepository.findById(codSala)
                .orElseThrow(() ->
                        new ErrorException404(
                                "No se encontró la sala con el codigo: " + codSala
                        ));
        List<ColeccionResponseDto> dataColeccion = coleccionService.getBySala(sala);

        if (dataColeccion == null)
            throw new ErrorException404(
                    "La sala con codigo: " + codSala +
                            " aún no tiene asociada ninguna colección."
            );

        return new SalaResponseDto2(sala.getCodSala(),
                        sala.getNombreSala(), dataColeccion);
    }

}
