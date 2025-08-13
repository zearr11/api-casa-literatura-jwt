package pe.gob.casadelaliteratura.biblioteca.controllers.libro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request.LibroDetalleNuevoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request.LibroNuevaCopiaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.LibroDetalleResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.copias.ILibroCopiaResponse;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ILibroService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    private final ILibroService libService;

    public LibroController(ILibroService libService) {
        this.libService = libService;
    }

    // http://localhost:8080/api/v1/libros || Resumen de libros con cantidades
    @GetMapping
    public ResponseEntity<List<LibroDetalleResponseDto>> obtenerResumenLibros(
            @RequestParam(required = false) String titulo, @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer year, @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editorial, @RequestParam(required = false) String coleccion,
            @RequestParam(required = false) String sala, @RequestParam(required = false) Integer cantidadCopias,
            @RequestParam(required = false) Integer cantidadDisponibles,
            @RequestParam(required = false) Integer cantidadPrestados,
            @RequestParam(required = false) Integer cantidadSoloLectura) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.getAllResumen(null, titulo, isbn, year, autor, editorial, coleccion, sala,
                        cantidadCopias, cantidadDisponibles, cantidadPrestados, cantidadSoloLectura));
    }

    // http://localhost:8080/api/v1/libros/LB00001
    @GetMapping("/{codigo}")
    public ResponseEntity<LibroDetalleResponseDto> obtenerResumenLibrosPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.getAllResumen(codigo, null, null, null, null, null,
                        null, null, null, null, null,
                        null).getFirst());
    }

    // http://localhost:8080/api/v1/libros/copias || Copias de libros con estado actual(PRESTADO, DISPONIBLE, LECTURA EN SALA)
    @GetMapping("/copias")
    public ResponseEntity<List<ILibroCopiaResponse>> obtenerCopiasLibros(
            @RequestParam(required = false) String isbn, @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer year, @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editorial, @RequestParam(required = false) String coleccion,
            @RequestParam(required = false) String sala, @RequestParam(required = false) Integer numeroCopia,
            @RequestParam(required = false) String estado) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.getAllCopias(null, isbn, titulo,
                        year, autor, editorial, coleccion, sala, numeroCopia, estado));
    }

    // http://localhost:8080/api/v1/libros/copias/LB00001
    @GetMapping("/copias/{codigo}")
    public ResponseEntity<List<ILibroCopiaResponse>> obtenerCopiasLibros(@PathVariable String codigo) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.getAllCopias(codigo, null, null, null, null,
                        null, null, null, null, null));
    }

    // http://localhost:8080/api/v1/libros/copias/LB00001/1
    @GetMapping("/copias/{codigo}/{numeroCopia}")
    public ResponseEntity<ILibroCopiaResponse> obtenerCopiasLibros(@PathVariable String codigo,
                                                                   @PathVariable Integer numeroCopia) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.getAllCopias(codigo, null, null, null, null,
                        null, null, null, numeroCopia, null).getFirst());
    }

    // http://localhost:8080/api/v1/libros || Registra en tabla libro_detalle
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarLibroGeneral(@Valid @RequestBody
                                                                        LibroDetalleNuevoRequestDto datosLibro) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(libService.saveOrUpdateLibroDetalle(null, datosLibro));
    }

    // http://localhost:8080/api/v1/libros/LB00001 || Actualiza en tabla libro_detalle
    @PutMapping("/{codigoLibro}")
    public ResponseEntity<MensajeDto<String>> actualizarLibroGeneral(@PathVariable String codigoLibro,
                                                                     @Valid @RequestBody
                                                                        LibroDetalleNuevoRequestDto datosLibro) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.saveOrUpdateLibroDetalle(codigoLibro, datosLibro));
    }

    // http://localhost:8080/api/v1/libros/copias || Registra en tabla libro una copia
    @PostMapping("/copias")
    public ResponseEntity<MensajeDto<String>> registrarCopiaLibro(@Valid @RequestBody
                                                                      LibroNuevaCopiaRequestDto datosLibro) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(libService.saveLibroCopia(datosLibro));
    }

}
