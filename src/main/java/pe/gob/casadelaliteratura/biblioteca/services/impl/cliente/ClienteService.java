package pe.gob.casadelaliteratura.biblioteca.services.impl.cliente;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteDataDto;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Cliente;
import pe.gob.casadelaliteratura.biblioteca.models.otros.AlmacenCodigos;
import pe.gob.casadelaliteratura.biblioteca.repositories.cliente.ClienteRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.cliente.IClienteService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.cliente.IDocumentacionService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.ClienteConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class ClienteService implements IClienteService {

    private final ClienteRepository repository;
    private final IDocumentacionService docService;
    private final AlmacenCodigosService acService;

    public ClienteService(ClienteRepository repository,
                          IDocumentacionService docService,
                          AlmacenCodigosService acService) {
        this.repository = repository;
        this.docService = docService;
        this.acService = acService;
    }

    @Override
    public List<ClienteDataDto> getAll() {
        List<ClienteDataDto> resultado = repository.findAll()
                .stream().map(ClienteConvert::modelToDto).toList();

        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron clientes.");

        return resultado;
    }

    @Override
    public ClienteDataDto getByCod(String codigo) {
        Cliente cliente = repository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con codigo " + codigo + " no existe."));

        return ClienteConvert.modelToDto(cliente);
    }

    @Override
    public ClienteDataDto getByNumeroDoc(String numeroDoc) {
        Cliente cliente = repository.findByNumeroDoc(numeroDoc)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con numero de documento " +
                        numeroDoc + " no existe."));

        return ClienteConvert.modelToDto(cliente);
    }

    @Override
    public MensajeDto<String> saveOrUpdate(String codCliente, ClienteData2Dto datosCliente,
                                           MultipartFile imgDocIdentidad,
                                           MultipartFile imgRecServicio) {
        Cliente cliente;
        String msg;
        String codClienteExistente = repository.validateDniAndNumDoc(
                                     datosCliente.getDatosPersonales().getNumeroDoc())
                                     .orElse(null);
        if (codCliente == null) {
            if (codClienteExistente != null)
                throw new ResourceNotFoundException("Ya existe un cliente con el numero de documento ingresado.");

            cliente = ClienteConvert.dto2ToCliente(datosCliente, imgDocIdentidad, imgRecServicio);
            cliente.setDocumentacion(docService.saveOrUpdate(cliente.getDocumentacion()));

            cliente.setCodCliente(acService.generateCodigo("CL"));
            acService.updateTable("CL");
            msg = "Cliente registrado satisfactoriamente";
        }
        else {
            if (codClienteExistente != null && !codClienteExistente.equals(codCliente))
                throw new ResourceNotFoundException("Ya existe un cliente con el numero de documento ingresado.");

            cliente = ClienteConvert.setCliente(
                    repository.findById(codCliente)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Cliente con codigo " + codCliente + " no existe.")),
                    ClienteConvert.dto2ToCliente(datosCliente, imgDocIdentidad, imgRecServicio)
            );
            msg = "Cliente actualizado satisfactoriamente";
        }

        repository.save(cliente);
        return new MensajeDto<>(msg);
    }

}
