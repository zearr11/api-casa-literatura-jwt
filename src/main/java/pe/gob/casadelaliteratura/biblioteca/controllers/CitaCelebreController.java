package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.casadelaliteratura.biblioteca.dtos.CitaCelebreDto;
import pe.gob.casadelaliteratura.biblioteca.services.impl.CitaCelebreService;

@RestController
@RequestMapping("/api/v1/citas")
public class CitaCelebreController {

    private final CitaCelebreService service;

    public CitaCelebreController(CitaCelebreService service) {
        this.service = service;
    }

    // http://localhost:8080/api/v1/citas
    @GetMapping
    public ResponseEntity<CitaCelebreDto> obtenerCita() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getCitaAleatoria());
    }

}
