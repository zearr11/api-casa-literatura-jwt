package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.autor.AutorDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IAutorService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

    private final IAutorService service;

    public AutorController(IAutorService service) {
        this.service = service;
    }

    // http://localhost:8080/api/v1/autores
    @GetMapping
    public ResponseEntity<List<AutorDataDto>> obtenerAutores() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    // http://localhost:8080/api/v1/autores/AT00001
    @GetMapping("/{codigoAutor}")
    public ResponseEntity<AutorDataDto> obtenerAutorPorId(@PathVariable String codigoAutor) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getByCod(codigoAutor));
    }

    // http://localhost:8080/api/v1/autores
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarAutor(@RequestBody AutorDataSimpleDto datosAutor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveOrUpdate(null, datosAutor));
    }

    // http://localhost:8080/api/v1/autores/AT00001
    @PutMapping("/{codigoAutor}")
    public ResponseEntity<MensajeDto<String>> actualizarAutor(@PathVariable String codigoAutor,
                                                              @RequestBody AutorDataSimpleDto datosAutor) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.saveOrUpdate(codigoAutor, datosAutor));
    }

}
