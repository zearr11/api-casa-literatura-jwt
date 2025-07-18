package pe.gob.casadelaliteratura.biblioteca.services.interfaces.cliente;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteDataDto;

import java.util.List;

public interface IClienteService {
    List<ClienteDataDto> getAll();
    ClienteDataDto getByCod(String codigo);
    ClienteDataDto getByNumeroDoc(String numeroDoc);
    MensajeDto<String> saveOrUpdate(String codCliente,
                                    ClienteData2Dto datosCliente,
                                    MultipartFile imgDocIdentidad,
                                    MultipartFile imgRecServicio);
}
