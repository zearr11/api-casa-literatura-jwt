package pe.gob.casadelaliteratura.biblioteca.controllers.libro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.sala.SalaResponseDto2;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ISalaService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {

    private final ISalaService salaService;

    public SalaController(ISalaService salaService) {
        this.salaService = salaService;
    }

    // http://localhost:8080/api/v1/salas
    @GetMapping
    public ResponseEntity<List<SalaResponseDto>> obtenerSalas() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salaService.getAllSalas());
    }

    // http://localhost:8080/api/v1/salas/SL00001
    @GetMapping("/{codigoSala}")
    public ResponseEntity<SalaResponseDto> obtenerSalaPorCod(@PathVariable String codigoSala) {
        return ResponseEntity.status(HttpStatus.OK).
                body(salaService.getByCodSala(codigoSala));
    }

    // http://localhost:8080/api/v1/salas/colecciones
    @GetMapping("/colecciones")
    public ResponseEntity<List<SalaResponseDto2>> obtenerSalasConColecciones() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salaService.getAllWithColecciones());
    }

    // http://localhost:8080/api/v1/salas/colecciones/SL00001
    @GetMapping("/colecciones/{codigoSala}")
    public ResponseEntity<SalaResponseDto2> obtenerSalaConColeccionesPorCod(@PathVariable String codigoSala) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salaService.getByCodWithColecciones(codigoSala));
    }

    // http://localhost:8080/api/v1/salas
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarSala(@Valid @RequestBody
                                                                SalaRequestDto datosSala) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salaService.saveOrUpdate(null, datosSala));
    }

    // http://localhost:8080/api/v1/salas/SL00001
    @PutMapping("/{codigoSala}")
    public ResponseEntity<MensajeDto<String>> actualizarSala(@PathVariable String codigoSala,
                                                             @Valid @RequestBody
                                                                SalaRequestDto datosSala) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salaService.saveOrUpdate(codigoSala, datosSala));
    }

}
