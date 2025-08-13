package pe.gob.casadelaliteratura.biblioteca.utils.converts;

import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteRequestDto;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Persona;

public class PersonaConvert {

    public static Persona clienteData2DtoToPersona(ClienteRequestDto nuevoCliente) {
        return Persona.builder()
                .nombres(nuevoCliente.getDatosPersonales().getNombres())
                .apellidos(nuevoCliente.getDatosPersonales().getApellidos())
                .tipoDoc(nuevoCliente.getDatosPersonales().getTipoDocumento())
                .numeroDoc(nuevoCliente.getDatosPersonales().getNumeroDoc())
                .fechaNacimiento(nuevoCliente.getDatosPersonales().getFechaNacimiento())
                .direccion(nuevoCliente.getDatosPersonales().getDireccion())
                .numeroPrincipal(nuevoCliente.getDatosContacto().getNumeroPrincipal())
                .correo(nuevoCliente.getDatosContacto().getCorreo())
                .build();
    }

    public static Persona setClienteData2DtoToPersona(ClienteRequestDto datosCliente, Persona persona) {

        persona.setNombres(datosCliente.getDatosPersonales().getNombres());
        persona.setApellidos(datosCliente.getDatosPersonales().getApellidos());
        persona.setTipoDoc(datosCliente.getDatosPersonales().getTipoDocumento());
        persona.setNumeroDoc(datosCliente.getDatosPersonales().getNumeroDoc());
        persona.setFechaNacimiento(datosCliente.getDatosPersonales().getFechaNacimiento());
        persona.setDireccion(datosCliente.getDatosPersonales().getDireccion());
        persona.setNumeroPrincipal(datosCliente.getDatosContacto().getNumeroPrincipal());
        persona.setCorreo(datosCliente.getDatosContacto().getCorreo());

        return persona;
    }

}
