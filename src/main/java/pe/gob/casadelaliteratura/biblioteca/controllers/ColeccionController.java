package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.coleccion.ColeccionDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IColeccionService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/colecciones")
public class ColeccionController {

    private final IColeccionService service;

    public ColeccionController(IColeccionService service) {
        this.service = service;
    }

    // http://localhost:8080/api/v1/colecciones
    @GetMapping
    public ResponseEntity<List<ColeccionDataDto>> obtenerColecciones() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    // http://localhost:8080/api/v1/colecciones/CC00001
    @GetMapping("/{codigoColeccion}")
    public ResponseEntity<ColeccionDataDto> obtenerColeccionPorId(@PathVariable String codigoColeccion) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getByCod(codigoColeccion));
    }

    // http://localhost:8080/api/v1/colecciones
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarColeccion(@RequestBody ColeccionDataSimpleDto datosColeccion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveOrUpdate(null, datosColeccion));
    }

    // http://localhost:8080/api/v1/colecciones/CC00001
    @PutMapping("/{codigoColeccion}")
    public ResponseEntity<MensajeDto<String>> actualizarColeccion(@PathVariable String codigoColeccion,
                                                                  @RequestBody ColeccionDataSimpleDto datosColeccion) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.saveOrUpdate(codigoColeccion, datosColeccion));
    }

}
