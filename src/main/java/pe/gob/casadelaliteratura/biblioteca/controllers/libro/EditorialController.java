package pe.gob.casadelaliteratura.biblioteca.controllers.libro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.editorial.EditorialResponseDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IEditorialService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/editoriales")
public class EditorialController {

    private final IEditorialService editorialService;

    public EditorialController(IEditorialService editorialService) {
        this.editorialService = editorialService;
    }

    // http://localhost:8080/api/v1/editoriales
    @GetMapping
    public ResponseEntity<List<EditorialResponseDto>> obtenerEditoriales() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(editorialService.getAllEditoriales());
    }

    // http://localhost:8080/api/v1/editoriales/ED00001
    @GetMapping("/{codigoEditorial}")
    public ResponseEntity<EditorialResponseDto> obtenerEditorialPorId(@PathVariable String codigoEditorial) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(editorialService.getByCodEditorial(codigoEditorial));
    }

    // http://localhost:8080/api/v1/editoriales
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarEditorial(@Valid @RequestBody
                                                                    EditorialRequestDto datosEditorial) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(editorialService.saveOrUpdate(null, datosEditorial));
    }

    // http://localhost:8080/api/v1/editoriales/ED00001
    @PutMapping("/{codigoEditorial}")
    public ResponseEntity<MensajeDto<String>> registrarEditorial(@PathVariable String codigoEditorial,
                                                                 @Valid @RequestBody
                                                                    EditorialRequestDto datosEditorial) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(editorialService.saveOrUpdate(codigoEditorial, datosEditorial));
    }

}
