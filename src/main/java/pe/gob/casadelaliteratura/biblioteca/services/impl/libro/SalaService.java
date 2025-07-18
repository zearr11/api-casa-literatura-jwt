package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Sala;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.SalaRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ISalaService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.SalaConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class SalaService implements ISalaService {

    private final SalaRepository repository;
    private final AlmacenCodigosService acService;

    public SalaService(SalaRepository repository,
                       AlmacenCodigosService acService) {
        this.repository = repository;
        this.acService = acService;
    }

    @Override
    public List<SalaDataDto> getAll() {
        List<SalaDataDto> resultado = repository.findAll().stream().map(
                SalaConvert::modelToDto
        ).toList();

        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron salas.");

        return resultado;
    }

    @Override
    public SalaDataDto getByCod(String codSala) {
        Sala sala = repository.findById(codSala)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sala con codigo " + codSala + " no existe."));

        return SalaConvert.modelToDto(sala);
    }

    @Override
    public List<SalaData2Dto> getAllWithColecciones() {
        List<SalaData2Dto> resultado = SalaConvert
                .projectionToListDto2(repository.salaWithColecciones());

        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron salas.");

        return resultado;
    }

    @Override
    public SalaData2Dto getByCodWithColecciones(String codSala) {
        SalaData2Dto resultado = SalaConvert
                .projectionToDto2(repository.salaWithColeccionesById(codSala));
        if (resultado == null) {
            if (getByCod(codSala) != null)
                throw new ResourceNotFoundException("Sala con codigo " +
                        codSala + " aún no cuenta con colecciones.");

            else
                throw new ResourceNotFoundException("Sala con codigo " +
                        codSala + " no existe.");
        }

        return resultado;
    }

    @Override
    public MensajeDto<String> saveOrUpdate(String codSala, SalaDataSimpleDto datosSala) {

        Sala sala;
        String msg;
        Sala salaExistente = repository.findByNombreSala(datosSala.getNombreSala())
                            .orElse(null);

        if (codSala == null){
            if (salaExistente != null)
                throw new ResourceNotFoundException("Ya existe una sala con el nombre ingresado.");

            sala = new Sala(acService.generateCodigo("SL"),
                    datosSala.getNombreSala());
            acService.updateTable("SL");

            msg = "Sala registrada satisfactoriamente.";
        }
        else {
            if (salaExistente != null && !salaExistente.getCodSala().equals(codSala))
                throw new ResourceNotFoundException("Ya existe una sala con el nombre ingresado.");

            sala = repository.findById(codSala)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Sala con codigo " + codSala + " no existe."));

            sala.setNombreSala(datosSala.getNombreSala());
            msg = "Sala actualizada satisfactoriamente.";
        }
        repository.save(sala);

        return new MensajeDto<>(msg);
    }

}
