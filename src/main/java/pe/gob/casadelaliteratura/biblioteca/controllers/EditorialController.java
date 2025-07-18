package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.editorial.EditorialDataSimpleDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.IEditorialService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/editoriales")
public class EditorialController {

    private final IEditorialService service;

    public EditorialController(IEditorialService service) {
        this.service = service;
    }

    // http://localhost:8080/api/v1/editoriales
    @GetMapping
    public ResponseEntity<List<EditorialDataDto>> obtenerEditoriales() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    // http://localhost:8080/api/v1/editoriales/ED00001
    @GetMapping("/{codigoEditorial}")
    public ResponseEntity<EditorialDataDto> obtenerEditorialPorId(@PathVariable String codigoEditorial) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getByCod(codigoEditorial));
    }

    // http://localhost:8080/api/v1/editoriales
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarEditorial(@RequestBody EditorialDataSimpleDto datosEditorial) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveOrUpdate(null, datosEditorial));
    }

    // http://localhost:8080/api/v1/editoriales/ED00001
    @PutMapping("/{codigoEditorial}")
    public ResponseEntity<MensajeDto<String>> registrarEditorial(@PathVariable String codigoEditorial,
                                                                 @RequestBody EditorialDataSimpleDto datosEditorial) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.saveOrUpdate(codigoEditorial, datosEditorial));
    }

}
