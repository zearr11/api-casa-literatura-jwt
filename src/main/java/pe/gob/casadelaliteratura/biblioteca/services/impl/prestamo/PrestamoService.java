package pe.gob.casadelaliteratura.biblioteca.services.impl.prestamo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.cliente.ClienteResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.persona.usuario.UsuarioResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.complements.*;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.*;
import pe.gob.casadelaliteratura.biblioteca.repositories.prestamo.*;
import pe.gob.casadelaliteratura.biblioteca.services.impl.login.LoginService;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.complements.DetallePrestamoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.PrestamoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.RangoFechasRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.RenovacionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.PrestamoResponseDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Libro;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Cliente;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.LibroRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.ClienteRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IClienteService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.persona.IUsuarioService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IPrestamoService;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoDevolucion;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoLibro;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException400;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoService implements IPrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final RenovacionRepository renovacionRepository;
    private final DetallePrestamoRepository detPrestRepository;
    private final DetalleDevolucionRepository detDevRepository;
    private final ProblemaDevolucionRepository problemaRepository;
    private final SolucionDevolucionRepository solucionDevolucionRepository;
    private final ClienteRepository clienteRepository;
    private final LibroRepository libroRepository;
    private final SancionDemoraRepository sancionDemoraRepository;
    private final AlmacenCodigosService acService;
    private final DevolucionRepository devolucionRepository;
    private final LoginService loginService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           RenovacionRepository renovacionRepository,
                           DetallePrestamoRepository detPrestRepository,
                           DetalleDevolucionRepository detDevRepository,
                           ProblemaDevolucionRepository problemaRepository,
                           SolucionDevolucionRepository solucionDevolucionRepository,
                           ClienteRepository clienteRepository,
                           LibroRepository libroRepository,
                           SancionDemoraRepository sancionDemoraRepository,
                           AlmacenCodigosService acService,
                           DevolucionRepository devolucionRepository,
                           LoginService loginService,
                           IClienteService clienteService,
                           IUsuarioService usuarioService) {
        this.prestamoRepository = prestamoRepository;
        this.renovacionRepository = renovacionRepository;
        this.detPrestRepository = detPrestRepository;
        this.detDevRepository = detDevRepository;
        this.problemaRepository = problemaRepository;
        this.solucionDevolucionRepository = solucionDevolucionRepository;
        this.clienteRepository = clienteRepository;
        this.libroRepository = libroRepository;
        this.sancionDemoraRepository = sancionDemoraRepository;
        this.acService = acService;
        this.devolucionRepository = devolucionRepository;
        this.loginService = loginService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    // falta implementar el usuario que registra el préstamo
    @Transactional
    @Override
    public MensajeDto<String> registrarPrestamo(PrestamoRequestDto datosPrestamo) {
        List<DetallePrestamo> librosPrestados = new ArrayList<>();

        Cliente cliente = clienteRepository
                .findById(datosPrestamo.getCodigoCliente())
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró al cliente con el codigo: " + datosPrestamo.getCodigoCliente()
                ));

        List<Prestamo> prestamosPendientes = prestamoRepository
                .findByCliente(cliente).orElse(null);

        if (prestamosPendientes != null) {
            for (Prestamo prestamoDetalle : prestamosPendientes) {
                if (prestamoDetalle.getEstadoDevolucion() == EstadoDevolucion.DEVOLUCION_PENDIENTE ||
                        prestamoDetalle.getEstadoDevolucion() == EstadoDevolucion.DEVOLUCION_PARCIAL)
                    throw new ErrorException409(
                            "El cliente con codigo " + cliente.getCodCliente() +
                            ", cuenta con un préstamo pendiente de devolución. " +
                            "No se puede continuar con la solicitud."
                    );

                Devolucion devolucion = devolucionRepository.findByPrestamo(prestamoDetalle)
                        .orElse(null);

                if (devolucion != null) {
                    SancionDemora sancionPendiente = sancionDemoraRepository.findByDevolucion(devolucion)
                            .orElse(null);

                    if (sancionPendiente != null &&
                            !LocalDate.now().isAfter(sancionPendiente.getFechaFinSancion()))
                        throw new ErrorException409(
                                "El cliente cuenta con una sanción por demora hasta el " +
                                        sancionPendiente.getFechaFinSancion() +
                                        ", por el préstamo registrado el " +
                                        prestamoDetalle.getFechaPrestamo() +
                                        ". No se puede continuar con la solicitud."
                        );
                }
            }
        }

        if (datosPrestamo.getDetallePrestamo().size() <= 4) {
            for (DetallePrestamoDto dtPrestamo : datosPrestamo.getDetallePrestamo()) {
                Long idLibroCopia = libroRepository
                        .findLibroByCodAndNumCopia(dtPrestamo.getCodigoLibro(), dtPrestamo.getNumeroCopia());

                if (idLibroCopia == null)
                    throw new ErrorException404(
                            "No se encontró el libro con el codigo: " + dtPrestamo.getCodigoLibro() +
                                    ". No se puede continuar con la solicitud."
                    );

                Libro libroCopia = libroRepository.findById(idLibroCopia).orElse(null);

                if (libroCopia != null && (libroCopia.getEstado() == EstadoLibro.PRESTADO ||
                        libroCopia.getEstado() == EstadoLibro.SOLO_PARA_LECTURA_EN_SALA))
                    throw new ErrorException409(
                            "El numero de copia del libro con codigo " +
                            dtPrestamo.getCodigoLibro() +
                            ", no esta habilitado para préstamos. No se puede continuar con la solicitud."
                    );

                int validLibroCopiaYaIngresado = (libroCopia != null) ?
                        librosPrestados.stream()
                                .filter(obj ->
                                        obj.getLibro().getLibroDetalle().getCodLibroDetalle()
                                                .equals(libroCopia.getLibroDetalle().getCodLibroDetalle()))
                                .toList().size() : 0;

                if (validLibroCopiaYaIngresado > 0)
                    throw new ErrorException409
                            ("Se ha ingresado un libro repetido. No se puede continuar con la solicitud.");

                librosPrestados.add(new DetallePrestamo(null, null, libroCopia));
            }
        }
        else
            throw new ErrorException400
                    ("No se puede realizar el préstamo de mas de 4 libros por proceso de atención.");

        Prestamo nuevoPrestamo = new Prestamo(acService.generateCodigo("PS"), LocalDate.now(),
                null, EstadoDevolucion.DEVOLUCION_PENDIENTE, cliente,
                loginService.obtenerUsuarioAutenticado());

        nuevoPrestamo = prestamoRepository.save(nuevoPrestamo);
        acService.updateTable("PS");

        for (DetallePrestamo dtPrestamo : librosPrestados) {
            dtPrestamo.setPrestamo(nuevoPrestamo);
            detPrestRepository.save(dtPrestamo);
        }

        String msg = "Se ha registrado el préstamo al cliente con codigo " +
                cliente.getCodCliente() +
                ". El codigo de registro del préstamo es " + nuevoPrestamo.getCodPrestamo() + ".";

        return new MensajeDto<>(msg);
    }

    @Transactional
    @Override
    public MensajeDto<String> renovarPrestamo(RenovacionRequestDto datosRenovacion) {
        /*
        1. Validar que no tenga mas de 3 renovaciones, si tiene 3 renovaciones lanzar excepcion
           indicando que el cliente ya no puede renovar mas.
        2. Validar que el prestamo no este vencido, si esta vencido no puede renovar.
        */
        return null;
    }

    @Override
    public List<PrestamoResponseDto> getPrestamosPersonalizado(String codPrestamo, String codCliente,
                                                               String codUsuario, EstadoDevolucion estadoDevolucion,
                                                               RangoFechasRequestDto datosFecha,
                                                               Boolean conSancionesActivas) {

        List<PrestamoResponseDto> lstPrestamos = new ArrayList<>();

        List<Prestamo> prestamosBd = prestamoRepository.findAll();
        LocalDate fechaActual = LocalDate.now();

        for (Prestamo prestamo : prestamosBd) {
            // Inicializacion
            PrestamoResponseDto prestamoResponseDto = new PrestamoResponseDto();
            List<DetallePrestamoResponseDto> detallePrestamo = new ArrayList<>();

            // Busqueda de datos de cliente y usuario
            UsuarioResponseDto usuarioResponseDto = usuarioService.getByCodUsuario(
                    prestamo.getUsuario().getCodUsuario()
            );
            ClienteResponseDto clienteResponseDto = clienteService.getByCodCliente(
                    prestamo.getCliente().getCodCliente()
            );

            // Verificar si el prestamo tiene devolucion
            Devolucion datosDevolucion = devolucionRepository.findByPrestamo(prestamo)
                    .orElse(null);

            // Verificar si el prestamo tiene renovaciones
            List<Renovacion> datosRenovaciones = renovacionRepository.findByPrestamo(prestamo);

            // Asignacion de datos opcionales
            if (datosDevolucion != null) {
                prestamoResponseDto.setFechaDevolucion(datosDevolucion.getFechaDevolucion());
                SancionDemora sancionDemora = sancionDemoraRepository.findByDevolucion(datosDevolucion)
                        .orElse(null);

                if (sancionDemora != null) {
                    SancionDemoraResponseDto sancion = new SancionDemoraResponseDto();

                    sancion.setDiasSuspension(sancionDemora.getDiasSuspension());
                    sancion.setFechaInicioSancion(sancionDemora.getFechaInicioSancion());
                    sancion.setFechaFinSancion(sancionDemora.getFechaFinSancion());
                    sancion.setEstadoSancion(
                            !fechaActual.isAfter(sancionDemora.getFechaFinSancion()) ?
                                    Estado.ACTIVO : Estado.INACTIVO
                    );

                    prestamoResponseDto.setDetalleSancion(sancion);
                }

            }

            if (!datosRenovaciones.isEmpty()) {
                List<RenovacionesResponseDto> renovacionesDePrestamo = new ArrayList<>();

                for (int i = 0; i < datosRenovaciones.size(); i++) {

                    Renovacion renovacion = datosRenovaciones.get(i);
                    RenovacionesResponseDto dataRenovacion = new RenovacionesResponseDto();

                    dataRenovacion.setNumeroSolicitud(i+1);
                    dataRenovacion.setFechaSolicitud(renovacion.getFechaSolicitud());
                    dataRenovacion.setMedioSolicitud(renovacion.getMedioSolicitud());
                    dataRenovacion.setNuevaFechaVencimientoAsignada(renovacion.getNuevaFechaVencimiento());

                    renovacionesDePrestamo.add(dataRenovacion);

                }

                prestamoResponseDto.setRenovaciones(renovacionesDePrestamo);
            }

            // Asignacion de datos obligatorios
            prestamoResponseDto.setDatosUsuario(usuarioResponseDto);
            prestamoResponseDto.setDatosCliente(clienteResponseDto);
            prestamoResponseDto.setCodigoPrestamo(prestamo.getCodPrestamo());
            prestamoResponseDto.setFechaPrestamo(prestamo.getFechaPrestamo());
            prestamoResponseDto.setEstadoDevolucion(prestamo.getEstadoDevolucion());
            prestamoResponseDto.setFechaVencimientoOriginal(prestamo.getFechaVencimiento());

            for (DetallePrestamo dtPrestamo : detPrestRepository.findByPrestamo(prestamo)) {
                DetallePrestamoResponseDto detallePrestamoResponseDto = new DetallePrestamoResponseDto();

                detallePrestamoResponseDto.setCodigoLibro(
                        dtPrestamo.getLibro().getLibroDetalle().getCodLibroDetalle()
                );
                detallePrestamoResponseDto.setTitulo(
                        dtPrestamo.getLibro().getLibroDetalle().getTitulo()
                );
                detallePrestamoResponseDto.setNumeroCopia(
                        dtPrestamo.getLibro().getNumeroCopia()
                );

                DetalleDevolucion detalleDevolucion = detDevRepository.findByDetallePrestamo(dtPrestamo)
                        .orElse(null);

                if (detalleDevolucion != null) {

                    DetalleDevolucionResponseDto detalleDevolucionResponseDto = new DetalleDevolucionResponseDto();

                    ProblemaDevolucion problemaDevolucion = problemaRepository
                            .findByDetalleDevolucion(detalleDevolucion).orElse(null);
                    SolucionDevolucion solucionDevolucion = problemaDevolucion != null ?
                            solucionDevolucionRepository
                                    .findByProblemaDevolucion(problemaDevolucion).orElse(null) :
                            null;

                    detalleDevolucionResponseDto.setEstadoEntrega(detalleDevolucion.getTipoEntrega());

                    if (problemaDevolucion != null) {

                        DetalleProblemaResponseDto problema = new DetalleProblemaResponseDto();

                        problema.setDetalleProblema(problemaDevolucion.getDetalle());
                        problema.setEstadoProblema(problemaDevolucion.getEstadoProblema());
                        problema.setFechaProblema(problemaDevolucion.getFechaProblema());
                        problema.setTipoProblema(problemaDevolucion.getTipoProblema());

                        detalleDevolucionResponseDto.setProblema(problema);

                    }

                    if (solucionDevolucion != null) {

                        DetalleSolucionResponseDto solucion = new DetalleSolucionResponseDto();

                        solucion.setDetalleSolucion(solucionDevolucion.getDetalle());
                        solucion.setFechaSolucion(solucionDevolucion.getFechaSolucion());

                        detalleDevolucionResponseDto.setSolucion(solucion);

                    }

                    detallePrestamoResponseDto.setDetalleDevolucion(detalleDevolucionResponseDto);
                }

                detallePrestamo.add(detallePrestamoResponseDto);
            }

            prestamoResponseDto.setDetallePrestamo(detallePrestamo);

            lstPrestamos.add(prestamoResponseDto);
        }

        if (codPrestamo != null) {
            lstPrestamos = lstPrestamos.stream().filter(
                    p -> p.getCodigoPrestamo().equals(codPrestamo)).toList();
        }
        if (codCliente != null) {
            lstPrestamos = lstPrestamos.stream().filter(
                    p -> p.getDatosCliente().getCodigo().equals(codCliente)).toList();
        }
        if (codUsuario != null) {
            lstPrestamos = lstPrestamos.stream().filter(
                    p -> p.getDatosUsuario().getCodigo().equals(codUsuario)).toList();
        }
        if (estadoDevolucion != null) {
            lstPrestamos = lstPrestamos.stream().filter(
                    p -> p.getEstadoDevolucion().equals(estadoDevolucion)).toList();
        }
        if (datosFecha != null) {
            lstPrestamos = lstPrestamos.stream().filter(
                    p -> !p.getFechaPrestamo().isBefore(datosFecha.getFechaDesde()) &&
                            !p.getFechaPrestamo().isAfter(datosFecha.getFechaHasta())
            ).toList();
        }
        if (conSancionesActivas != null) {
            lstPrestamos = lstPrestamos.stream().filter(
                    p -> p.getDetalleSancion() != null &&
                            p.getDetalleSancion().getEstadoSancion().equals(
                                    (conSancionesActivas) ?
                                    Estado.ACTIVO : Estado.INACTIVO)
            ).toList();
        }

        if (lstPrestamos.isEmpty())
            throw new ErrorException404("No se encontraron prestamos.");

        return lstPrestamos;
    }

}