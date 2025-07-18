package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.sala.SalaDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ISalaService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {

    private final ISalaService service;

    public SalaController(ISalaService service) {
        this.service = service;
    }

    // http://localhost:8080/api/v1/salas
    @GetMapping
    public ResponseEntity<List<SalaDataDto>> obtenerSalas() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    // http://localhost:8080/api/v1/salas/SL00001
    @GetMapping("/{codigoSala}")
    public ResponseEntity<SalaDataDto> obtenerSalaPorCod(@PathVariable String codigoSala) {
        return ResponseEntity.status(HttpStatus.OK).
                body(service.getByCod(codigoSala));
    }

    // http://localhost:8080/api/v1/salas/colecciones
    @GetMapping("/colecciones")
    public ResponseEntity<List<SalaData2Dto>> obtenerSalasConColecciones() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllWithColecciones());
    }

    // http://localhost:8080/api/v1/salas/colecciones/SL00001
    @GetMapping("/colecciones/{codigoSala}")
    public ResponseEntity<SalaData2Dto> obtenerSalaConColeccionesPorCod(@PathVariable String codigoSala) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getByCodWithColecciones(codigoSala));
    }

    // http://localhost:8080/api/v1/salas
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarSala(@RequestBody SalaDataSimpleDto datosSala) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveOrUpdate(null, datosSala));
    }

    // http://localhost:8080/api/v1/salas/SL00001
    @PutMapping("/{codigoSala}")
    public ResponseEntity<MensajeDto<String>> actualizarSala(@PathVariable String codigoSala,
                                                             @RequestBody SalaDataSimpleDto datosSala) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.saveOrUpdate(codigoSala, datosSala));
    }

}
