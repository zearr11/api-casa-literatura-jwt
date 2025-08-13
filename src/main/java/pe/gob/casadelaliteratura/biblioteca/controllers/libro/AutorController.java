package pe.gob.casadelaliteratura.biblioteca.controllers.libro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.autor.AutorResponseDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IAutorService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

    private final IAutorService autorService;

    public AutorController(IAutorService autorService) {
        this.autorService = autorService;
    }

    // http://localhost:8080/api/v1/autores
    @GetMapping
    public ResponseEntity<List<AutorResponseDto>> obtenerAutores() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(autorService.getAllAutores());
    }

    // http://localhost:8080/api/v1/autores/AT00001
    @GetMapping("/{codigoAutor}")
    public ResponseEntity<AutorResponseDto> obtenerAutorPorId(@PathVariable String codigoAutor) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(autorService.getByCodAutor(codigoAutor));
    }

    // http://localhost:8080/api/v1/autores
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarAutor(@Valid @RequestBody AutorRequestDto datosAutor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(autorService.saveOrUpdate(null, datosAutor));
    }

    // http://localhost:8080/api/v1/autores/AT00001
    @PutMapping("/{codigoAutor}")
    public ResponseEntity<MensajeDto<String>> actualizarAutor(@PathVariable String codigoAutor,
                                                              @Valid @RequestBody AutorRequestDto datosAutor) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(autorService.saveOrUpdate(codigoAutor, datosAutor));
    }

}
