package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.ClienteDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements.ClienteDataContactoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements.ClienteDataDocDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.cliente.complements.ClienteDataPersonalDto;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Cliente;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Documentacion;

import java.io.IOException;

public class ClienteConvert {

    public static ClienteDataDto modelToDto(Cliente cliente) {

        ClienteDataPersonalDto dataPersonal = new ClienteDataPersonalDto(
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getTipoDoc(),
                cliente.getNumeroDoc(),
                cliente.getFechaNacimiento(),
                cliente.getDireccion()
        );

        ClienteDataContactoDto dataContacto = new ClienteDataContactoDto(
                cliente.getNumeroPrincipal(),
                cliente.getNumeroSecundario(),
                cliente.getCorreo()
        );

        ClienteDataDocDto dataDocumentacion = new ClienteDataDocDto(
                cliente.getDocumentacion().getImgDocIdentidad(),
                cliente.getDocumentacion().getImgRecServicio()
        );

        return new ClienteDataDto(cliente.getCodCliente(), dataPersonal, dataContacto, dataDocumentacion);
    }

    public static Cliente dto2ToCliente(ClienteData2Dto nuevoCliente,
                                        MultipartFile imgDocIden,
                                        MultipartFile imgRecServ) {
        try {
            return Cliente.builder()
                    .nombres(nuevoCliente.getDatosPersonales().getNombres())
                    .apellidos(nuevoCliente.getDatosPersonales().getApellidos())
                    .tipoDoc(nuevoCliente.getDatosPersonales().getTipoDocumento())
                    .numeroDoc(nuevoCliente.getDatosPersonales().getNumeroDoc())
                    .fechaNacimiento(nuevoCliente.getDatosPersonales().getFechaNacimiento())
                    .direccion(nuevoCliente.getDatosPersonales().getDireccion())
                    .numeroPrincipal(nuevoCliente.getDatosContacto().getNumeroPrincipal())
                    .numeroSecundario(nuevoCliente.getDatosContacto().getNumeroSecundario())
                    .correo(nuevoCliente.getDatosContacto().getCorreo())
                    .documentacion(
                            Documentacion.builder()
                                    .imgDocIdentidad(imgDocIden.getBytes())
                                    .imgRecServicio(imgRecServ.getBytes())
                                    .build()
                    )
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Error con las imágenes cargadas");
        }
    }

    public static Cliente setCliente(Cliente antiguo, Cliente nuevo) {

        antiguo.setNombres(nuevo.getNombres());
        antiguo.setApellidos(nuevo.getApellidos());
        antiguo.setTipoDoc(nuevo.getTipoDoc());
        antiguo.setNumeroDoc(nuevo.getNumeroDoc());
        antiguo.setFechaNacimiento(nuevo.getFechaNacimiento());
        antiguo.setDireccion(nuevo.getDireccion());
        antiguo.setNumeroPrincipal(nuevo.getNumeroPrincipal());
        antiguo.setNumeroSecundario(nuevo.getNumeroSecundario());
        antiguo.setCorreo(nuevo.getCorreo());
        antiguo.getDocumentacion().setImgRecServicio(nuevo.getDocumentacion().getImgRecServicio());
        antiguo.getDocumentacion().setImgDocIdentidad(nuevo.getDocumentacion().getImgDocIdentidad());

        return antiguo;
    }

}
