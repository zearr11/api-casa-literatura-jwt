package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.PrestamoDataDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IPrestamoService;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final IPrestamoService prestamoService;

    public PrestamoController(IPrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarPrestamo(@RequestBody PrestamoDataDto datosPrestamo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.registrar(datosPrestamo));
    }

}
