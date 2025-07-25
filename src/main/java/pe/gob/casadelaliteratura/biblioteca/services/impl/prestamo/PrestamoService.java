package pe.gob.casadelaliteratura.biblioteca.services.impl.prestamo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.complements.DetallePrestamoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.PrestamoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Libro;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Cliente;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetallePrestamo;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Prestamo;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.LibroRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.persona.ClienteRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.prestamo.DetallePrestamoRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.prestamo.PrestamoRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IPrestamoService;
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
    private final DetallePrestamoRepository detPrestRepository;
    private final ClienteRepository clienteRepository;
    private final LibroRepository libroRepository;
    private final AlmacenCodigosService acService;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           DetallePrestamoRepository detPrestRepository,
                           ClienteRepository clienteRepository,
                           LibroRepository libroRepository, AlmacenCodigosService acService) {
        this.prestamoRepository = prestamoRepository;
        this.detPrestRepository = detPrestRepository;
        this.clienteRepository = clienteRepository;
        this.libroRepository = libroRepository;
        this.acService = acService;
    }

    // falta implementar el usuario que registra el prestamo
    @Transactional
    @Override
    public MensajeDto<String> registrar(PrestamoRequestDto datosPrestamo) {
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
                            " cuenta con un préstamo pendiente de devolución. " +
                            "No se puede continuar con la solicitud."
                    );
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
                            " no esta habilitado para préstamos. No se puede continuar con la solicitud."
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
                null, EstadoDevolucion.DEVOLUCION_PENDIENTE, cliente, null);

        nuevoPrestamo = prestamoRepository.save(nuevoPrestamo);

        for (DetallePrestamo dtPrestamo : librosPrestados) {
            dtPrestamo.setPrestamo(nuevoPrestamo);
            detPrestRepository.save(dtPrestamo);
        }

        String msg = "Se ha registrado el préstamo al cliente con codigo " +
                cliente.getCodCliente() +
                ". El codigo de registro del préstamo es " + nuevoPrestamo.getCodPrestamo() + ".";

        return new MensajeDto<>(msg);
    }

}