package pe.gob.casadelaliteratura.biblioteca.controllers.libro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.coleccion.ColeccionResponseDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IColeccionService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/colecciones")
public class ColeccionController {

    private final IColeccionService coleccionService;

    public ColeccionController(IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    // http://localhost:8080/api/v1/colecciones
    @GetMapping
    public ResponseEntity<List<ColeccionResponseDto>> obtenerColecciones() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(coleccionService.getAllColecciones());
    }

    // http://localhost:8080/api/v1/colecciones/CC00001
    @GetMapping("/{codigoColeccion}")
    public ResponseEntity<ColeccionResponseDto> obtenerColeccionPorId(@PathVariable String codigoColeccion) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(coleccionService.getByCodColeccion(codigoColeccion));
    }

    // http://localhost:8080/api/v1/colecciones
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarColeccion(@Valid @RequestBody
                                                                     ColeccionRequestDto datosColeccion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(coleccionService.saveOrUpdate(null, datosColeccion));
    }

    // http://localhost:8080/api/v1/colecciones/CC00001
    @PutMapping("/{codigoColeccion}")
    public ResponseEntity<MensajeDto<String>> actualizarColeccion(@PathVariable String codigoColeccion,
                                                                  @Valid @RequestBody
                                                                        ColeccionRequestDto datosColeccion) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(coleccionService.saveOrUpdate(codigoColeccion, datosColeccion));
    }

}
