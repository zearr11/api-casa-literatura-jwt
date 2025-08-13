package pe.gob.casadelaliteratura.biblioteca.controllers.persona;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteResponseDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IClienteService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final IClienteService clientService;

    public ClienteController(IClienteService clientService) {
        this.clientService = clientService;
    }

    // http://localhost:8080/api/v1/clientes
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> obtenerClientes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientService.getAllClientes());
    }

    // http://localhost:8080/api/v1/clientes/codigo/CL00001
    @GetMapping("/codigo/{codigoCliente}")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorId(@PathVariable String codigoCliente) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientService.getByCodCliente(codigoCliente));
    }

    // http://localhost:8080/api/v1/clientes/numero-doc/12345678
    @GetMapping("/numero-doc/{numDoc}")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorNumDoc(@PathVariable String numDoc) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientService.getByNumeroDoc(numDoc));
    }

    // http://localhost:8080/api/v1/clientes
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarCliente(@Valid @RequestPart ClienteRequestDto datosCliente,
                                                               @RequestPart(required = false) MultipartFile imgDocIdentidad,
                                                               @RequestPart(required = false) MultipartFile imgRecServicio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.saveOrUpdate(null, datosCliente, imgDocIdentidad, imgRecServicio));
    }

    // http://localhost:8080/api/v1/clientes/CL00001
    @PutMapping("/{codigoCliente}")
    public ResponseEntity<MensajeDto<String>> actualizarCliente(@PathVariable String codigoCliente,
                                                                @Valid @RequestPart ClienteRequestDto datosCliente,
                                                                @RequestPart(required = false) MultipartFile imgDocIdentidad,
                                                                @RequestPart(required = false) MultipartFile imgRecServicio) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientService.saveOrUpdate(codigoCliente, datosCliente, imgDocIdentidad, imgRecServicio));
    }

}
