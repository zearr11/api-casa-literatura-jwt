package pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteResponseDto;
import java.util.List;

public interface IClienteService {

    // List<Cliente> getAllClientesRegular();
    // Cliente getClienteRegularById();
    MensajeDto<String> saveOrUpdate(String codCliente, ClienteRequestDto datosCliente,
                                    MultipartFile imgDocIdentidad, MultipartFile imgRecServicio);

    List<ClienteResponseDto> getAllClientes();
    ClienteResponseDto getByCodCliente(String codCliente);
    ClienteResponseDto getByCodPersona(String codPersona);
    ClienteResponseDto getByNumeroDoc(String numeroDoc);

}
