package pe.gob.casadelaliteratura.biblioteca.services.impl.persona;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Cliente;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Persona;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.ClienteRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.PersonaRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections.ClienteProjection;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.impl.CloudinaryService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IClienteService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.ClienteConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.PersonaConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import pe.gob.casadelaliteratura.biblioteca.utils.validations.ArchivoValidacion;
import pe.gob.casadelaliteratura.biblioteca.utils.validations.ValidacionDocIden;

import java.util.List;

@Service
public class ClienteService implements IClienteService {

    private final ClienteRepository clientRepository;
    private final PersonaRepository personRepository;
    private final AlmacenCodigosService acService;
    private final CloudinaryService cloudinaryService;

    public ClienteService(ClienteRepository clientRepository,
                          PersonaRepository personRepository,
                          AlmacenCodigosService acService,
                          CloudinaryService cloudinaryService) {
        this.clientRepository = clientRepository;
        this.personRepository = personRepository;
        this.acService = acService;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdate(String codCliente, ClienteRequestDto datosCliente,
                                           MultipartFile imgDocIdentidad, MultipartFile imgRecServicio) {
        // Validacion Doc Identidad
        ValidacionDocIden.validationDoc(datosCliente.getDatosPersonales().getNumeroDoc(),
                datosCliente.getDatosPersonales().getTipoDocumento());

        // Carga de atributos
        Cliente cliente;
        Persona persona;
        String msg;
        ClienteProjection clienteExistente = clientRepository.findByCustomized(
                        datosCliente.getDatosPersonales().getNumeroDoc(), null, null)
                .orElse(null);

        if (codCliente == null) { // NUEVO CLIENTE
            if (clienteExistente != null)
                throw new ErrorException409("Ya existe un cliente con el numero de documento ingresado.");

            ArchivoValidacion.validacionImg(imgDocIdentidad, "documento de identidad");
            ArchivoValidacion.validacionImg(imgRecServicio, "recibo de servicio");

            // Asignacion de atributos
            String urlDocIden = cloudinaryService.subirArchivoImg(imgDocIdentidad);
            String urlRecServ = cloudinaryService.subirArchivoImg(imgRecServicio);

            persona = PersonaConvert.clienteData2DtoToPersona(datosCliente);
            cliente = ClienteConvert.clienteData2DtoToCliente(datosCliente, urlDocIden, urlRecServ);

            // Asignacion de id's
            cliente.setCodCliente(acService.generateCodigo("CL"));
            persona.setCodPersona(acService.generateCodigo("PA"));

            acService.updateTable("CL");
            acService.updateTable("PA");

            // Creacion de persona y asociacion de dato
            cliente.setPersona(personRepository.save(persona));

            // Mensaje de confirmacion
            msg = "Cliente registrado satisfactoriamente.";
        }
        else { // EDITAR CLIENTE
            if (clienteExistente != null && !clienteExistente.getCodCliente().equals(codCliente))
                throw new ErrorException409("Ya existe un cliente con el numero de documento ingresado.");

            // Busqueda de cliente y persona en bd
            Cliente clienteBusqueda = clientRepository.findById(codCliente)
                    .orElseThrow(() ->
                            new ErrorException404(
                                    "No se encontró al cliente con el codigo: " + codCliente
                            ));

            Persona personaBusqueda = clienteBusqueda.getPersona();
            String urlDocIden = clienteBusqueda.getUrlDocIdentidad();
            String urlRecServ = clienteBusqueda.getUrlRecServicio();

            if (imgDocIdentidad != null) {
                ArchivoValidacion.validacionImg(imgDocIdentidad, "documento de identidad");

                boolean docIdenSonIguales = cloudinaryService.sonImagenesIguales(imgDocIdentidad, urlDocIden);
                if (!docIdenSonIguales){
                    cloudinaryService.eliminarArchivo(urlDocIden);
                    urlDocIden = cloudinaryService.subirArchivoImg(imgDocIdentidad);
                }
            }
            if (imgRecServicio != null) {
                ArchivoValidacion.validacionImg(imgRecServicio, "recibo de servicio");

                boolean recServSonIguales = cloudinaryService.sonImagenesIguales(imgRecServicio, urlRecServ);
                if (!recServSonIguales){
                    cloudinaryService.eliminarArchivo(urlRecServ);
                    urlRecServ = cloudinaryService.subirArchivoImg(imgRecServicio);
                }
            }

            // Asignacion de nuevos atributos a las entidades actuales
            persona = PersonaConvert.setClienteData2DtoToPersona(datosCliente, personaBusqueda);
            cliente = ClienteConvert.setClienteData2DtoToCliente(datosCliente,
                    urlDocIden, urlRecServ, clienteBusqueda, persona);

            // Mensaje de actualizacion
            msg = "Cliente actualizado satisfactoriamente.";
        }

        // Creacion y/o actualizacion de cliente
        clientRepository.save(cliente);
        return new MensajeDto<>(msg);
    }

    @Override
    public List<ClienteResponseDto> getAllClientes() {
        List<ClienteResponseDto> resultado = clientRepository.findAll()
                .stream().map(ClienteConvert::clienteToClienteDataDto).toList();

        if (resultado.isEmpty())
            throw new ErrorException404("No se encontraron clientes.");

        return resultado;
    }

    @Override
    public ClienteResponseDto getByCodCliente(String codCliente) {
        return clientRepository
                .findByCustomized(null, codCliente, null)
                .map(ClienteConvert::clienteProjectionToClienteDataDto)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró al cliente con el codigo: " + codCliente
                ));
    }

    @Override
    public ClienteResponseDto getByCodPersona(String codPersona) {
        return clientRepository
                .findByCustomized(null, null, codPersona)
                .map(ClienteConvert::clienteProjectionToClienteDataDto)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró al cliente con el codigo: " + codPersona
                ));
    }

    @Override
    public ClienteResponseDto getByNumeroDoc(String numeroDoc) {
        return clientRepository
                .findByCustomized(numeroDoc, null, null)
                .map(ClienteConvert::clienteProjectionToClienteDataDto)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró al cliente con el numero de documento: " + numeroDoc
                ));
    }
}
