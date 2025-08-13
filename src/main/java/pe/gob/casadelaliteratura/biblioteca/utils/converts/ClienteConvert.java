package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataContactoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataUrlDocsDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.complements.DataPersonalDto;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Persona;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Cliente;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections.ClienteProjection;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoDoc;

public class ClienteConvert {

    public static ClienteResponseDto clienteToClienteDataDto(Cliente cliente) {

        DataPersonalDto dataPersonal = new DataPersonalDto(
                cliente.getPersona().getNombres(),
                cliente.getPersona().getApellidos(),
                cliente.getPersona().getTipoDoc(),
                cliente.getPersona().getNumeroDoc(),
                cliente.getPersona().getFechaNacimiento(),
                cliente.getPersona().getDireccion()
        );

        DataContactoDto dataContacto = new DataContactoDto(
                cliente.getPersona().getNumeroPrincipal(),
                cliente.getNumeroSecundario(),
                cliente.getPersona().getCorreo()
        );

        DataUrlDocsDto dataDocumentacion = new DataUrlDocsDto(
                cliente.getUrlDocIdentidad(),
                cliente.getUrlRecServicio()
        );

        return new ClienteResponseDto(
                cliente.getCodCliente(), dataPersonal, dataContacto, dataDocumentacion
        );
    }

    public static ClienteResponseDto clienteProjectionToClienteDataDto(ClienteProjection cliente) {

        DataPersonalDto dataPersonal = new DataPersonalDto(
                cliente.getNombres(),
                cliente.getApellidos(),
                TipoDoc.valueOf(cliente.getTipoDoc()),
                cliente.getNumeroDoc(),
                cliente.getFechaNacimiento(),
                cliente.getDireccion()
        );

        DataContactoDto dataContacto = new DataContactoDto(
                cliente.getNumeroPrincipal(),
                cliente.getNumeroSecundario(),
                cliente.getCorreo()
        );

        DataUrlDocsDto dataDocumentacion = new DataUrlDocsDto(
                cliente.getUrlDocIdentidad(),
                cliente.getUrlRecServicio()
        );

        return new ClienteResponseDto(
                cliente.getCodCliente(), dataPersonal, dataContacto, dataDocumentacion
        );
    }

    public static Cliente clienteData2DtoToCliente(ClienteRequestDto nuevoCliente,
                                                   String urlDocIden, String urlRecServ) {

        String numSec = nuevoCliente.getDatosContacto().getNumeroSecundario() != null ?
                nuevoCliente.getDatosContacto().getNumeroSecundario() : "";

        return Cliente.builder()
                .urlDocIdentidad(urlDocIden)
                .urlRecServicio(urlRecServ)
                .numeroSecundario(numSec)
                .build();
    }

    public static Cliente setClienteData2DtoToCliente(ClienteRequestDto datosCliente, String urlDocIden,
                                                      String urlRecServ, Cliente cliente, Persona persona) {

        String numSec = datosCliente.getDatosContacto().getNumeroSecundario() != null ?
                datosCliente.getDatosContacto().getNumeroSecundario() : "";

        cliente.setNumeroSecundario(numSec);
        cliente.setUrlDocIdentidad(urlDocIden);
        cliente.setUrlRecServicio(urlRecServ);
        cliente.setPersona(persona);

        return cliente;
    }
}
