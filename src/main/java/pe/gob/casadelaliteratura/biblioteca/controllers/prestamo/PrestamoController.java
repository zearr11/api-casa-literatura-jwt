package pe.gob.casadelaliteratura.biblioteca.controllers.prestamo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.PrestamoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.RangoFechasRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.RenovacionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.PrestamoResponseDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IPrestamoService;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoDevolucion;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final IPrestamoService prestamoService;

    public PrestamoController(IPrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // http://localhost:8080/api/v1/prestamos
    @GetMapping
    public ResponseEntity<List<PrestamoResponseDto>> obtenerPrestamos(@RequestParam(required = false)
                                                                          EstadoDevolucion estadoDevolucion,
                                                                      @RequestBody(required = false)
                                                                            RangoFechasRequestDto datosFecha) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.getPrestamosPersonalizado(
                        null, null, null, estadoDevolucion, datosFecha));
    }

    // http://localhost:8080/api/v1/prestamos/cod-prestamo/PS00001
    @GetMapping("/cod-prestamo/{codPrestamo}")
    public ResponseEntity<PrestamoResponseDto> obtenerPrestamoPorCodPrestamo(@PathVariable String codPrestamo,
                                                                             @RequestParam(required = false)
                                                                                EstadoDevolucion estadoDevolucion,
                                                                             @RequestBody(required = false)
                                                                                 RangoFechasRequestDto datosFecha) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.getPrestamosPersonalizado(
                        codPrestamo, null, null, estadoDevolucion, datosFecha)
                        .getFirst());
    }

    // http://localhost:8080/api/v1/prestamos/cod-cliente/CL00001
    @GetMapping("/cod-cliente/{codCliente}")
    public ResponseEntity<List<PrestamoResponseDto>> obtenerPrestamoPorCodCliente(@PathVariable String codCliente,
                                                                                    @RequestParam(required = false)
                                                                                        EstadoDevolucion estadoDevolucion,
                                                                                  @RequestBody(required = false)
                                                                                      RangoFechasRequestDto datosFecha) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.getPrestamosPersonalizado(
                        null, codCliente, null, estadoDevolucion, datosFecha));
    }

    // http://localhost:8080/api/v1/prestamos/cod-usuario/US00001
    @GetMapping("/cod-usuario/{codUsuario}")
    public ResponseEntity<List<PrestamoResponseDto>> obtenerPrestamoPorCodUsuario(@PathVariable String codUsuario,
                                                                                  @RequestParam(required = false)
                                                                                      EstadoDevolucion estadoDevolucion,
                                                                                  @RequestBody(required = false)
                                                                                      RangoFechasRequestDto datosFecha) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.getPrestamosPersonalizado(
                        null, null, codUsuario, estadoDevolucion, datosFecha));
    }

    // http://localhost:8080/api/v1/prestamos
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarPrestamo(@Valid @RequestBody
                                                                    PrestamoRequestDto datosPrestamo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.registrarPrestamo(datosPrestamo));
    }

    // http://localhost:8080/api/v1/prestamos/renovacion
    @PostMapping("/renovacion")
    public ResponseEntity<MensajeDto<String>> registrarRenovacion(@Valid @RequestBody
                                                                      RenovacionRequestDto datosRenovacion) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(prestamoService.renovarPrestamo(datosRenovacion));
    }

}
