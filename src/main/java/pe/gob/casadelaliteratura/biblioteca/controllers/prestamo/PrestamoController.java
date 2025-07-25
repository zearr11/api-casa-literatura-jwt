package pe.gob.casadelaliteratura.biblioteca.controllers.prestamo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.PrestamoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IPrestamoService;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final IPrestamoService prestamoService;

    public PrestamoController(IPrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // http://localhost:8080/api/v1/prestamos
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarPrestamo(@Valid @RequestBody
                                                                    PrestamoRequestDto datosPrestamo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.registrar(datosPrestamo));
    }

}
