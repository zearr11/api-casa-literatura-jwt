package pe.gob.casadelaliteratura.biblioteca.controllers.prestamo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.DevolucionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.SolucionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IDevolucionService;

@RestController
@RequestMapping("/api/v1/devoluciones")
public class DevolucionController {

    private final IDevolucionService devolucionService;

    public DevolucionController(IDevolucionService devolucionService) {
        this.devolucionService = devolucionService;
    }

    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarDevolucion(@Valid @RequestBody
                                                                      DevolucionRequestDto datosDevolucion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(devolucionService.registrarDevolucion(datosDevolucion));
    }

    @PostMapping("/soluciones")
    public ResponseEntity<MensajeDto<String>> registrarSolucionDevolucion(@Valid @RequestBody
                                                                              SolucionRequestDto datosSolucion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(devolucionService.registrarSolucionDevolucion(datosSolucion));
    }

}
