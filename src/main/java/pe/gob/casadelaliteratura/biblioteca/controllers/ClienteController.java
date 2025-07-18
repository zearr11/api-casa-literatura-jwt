package pe.gob.casadelaliteratura.biblioteca.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteDataDto;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.cliente.IClienteService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final IClienteService service;

    public ClienteController(IClienteService service) {
        this.service = service;
    }

    // http://localhost:8080/api/v1/clientes
    @GetMapping
    public ResponseEntity<List<ClienteDataDto>> obtenerClientes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    // http://localhost:8080/api/v1/clientes/codigo/CL00001
    @GetMapping("/codigo/{codigoCliente}")
    public ResponseEntity<ClienteDataDto> obtenerClientePorId(@PathVariable String codigoCliente) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getByCod(codigoCliente));
    }

    // http://localhost:8080/api/v1/clientes/numero-doc/12345678
    @GetMapping("/numero-doc/{numDoc}")
    public ResponseEntity<ClienteDataDto> obtenerClientePorNumDoc(@PathVariable String numDoc) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getByNumeroDoc(numDoc));
    }

    // http://localhost:8080/api/v1/clientes
    @PostMapping
    public ResponseEntity<MensajeDto<String>> registrarCliente(@RequestPart ClienteData2Dto datosCliente,
                                                               @RequestPart MultipartFile imgDocIdentidad,
                                                               @RequestPart MultipartFile imgRecServicio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveOrUpdate(null, datosCliente, imgDocIdentidad, imgRecServicio));
    }

    // http://localhost:8080/api/v1/clientes/CL00001
    @PutMapping("/{codigoCliente}")
    public ResponseEntity<MensajeDto<String>> actualizarCliente(@PathVariable String codigoCliente,
                                                                @RequestPart ClienteData2Dto datosCliente,
                                                                @RequestPart MultipartFile imgDocIdentidad,
                                                                @RequestPart MultipartFile imgRecServicio) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.saveOrUpdate(codigoCliente, datosCliente, imgDocIdentidad, imgRecServicio));
    }

}
