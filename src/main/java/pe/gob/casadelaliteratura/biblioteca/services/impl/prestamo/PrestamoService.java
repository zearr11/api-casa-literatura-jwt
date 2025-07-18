package pe.gob.casadelaliteratura.biblioteca.services.impl.prestamo;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.PrestamoDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.complements.DetallePrestamoDataDto;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Cliente;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Libro;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetallePrestamo;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Prestamo;
import pe.gob.casadelaliteratura.biblioteca.repositories.cliente.ClienteRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.LibroRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.prestamo.DetallePrestamoRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.prestamo.PrestamoRepository;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IPrestamoService;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoDevolucion;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoLibro;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
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

    @Override
    public MensajeDto<String> registrar(PrestamoDataDto datosPrestamo) {

        List<DetallePrestamo> librosPrestados = new ArrayList<>();

        Cliente cliente = clienteRepository
                .findById(datosPrestamo.getCodCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con codigo "
                        + datosPrestamo.getCodCliente() + " no existe."));

        List<Prestamo> validacionPrestamo = prestamoRepository
                .findByCliente(cliente).orElse(null);

        if (validacionPrestamo != null){
            for (Prestamo prestamoDetalle : validacionPrestamo) {
                if (prestamoDetalle.getEstadoDevolucion() == EstadoDevolucion.DEVOLUCION_PENDIENTE ||
                        prestamoDetalle.getEstadoDevolucion() == EstadoDevolucion.DEVOLUCION_PARCIAL)
                    throw new ResourceNotFoundException("El cliente con codigo "
                            + cliente.getCodCliente() + " ya cuenta con un prestamo pendiente de devolucion. " +
                                    "No se ha podido concretar el registro.");
            }
        }

        if (datosPrestamo.getDetallePrestamo().size() <= 4) {
            for (DetallePrestamoDataDto dtPrestamo : datosPrestamo.getDetallePrestamo()) {
                Long idLibroCopia = libroRepository
                        .findLibroByCodAndNumCopia(dtPrestamo.getCodLibro(), dtPrestamo.getNumeroCopia());

                if (idLibroCopia == null)
                    throw new ResourceNotFoundException("El libro con codigo " +
                            dtPrestamo.getCodLibro() + " no existe. No se ha podido concretar el registro.");

                Libro libroCopia = libroRepository.findById(idLibroCopia).orElse(null);

                if (libroCopia != null && (libroCopia.getEstado() == EstadoLibro.PRESTADO ||
                        libroCopia.getEstado() == EstadoLibro.SOLO_PARA_LECTURA_EN_SALA))
                    throw new ResourceNotFoundException("El numero de copia del libro con codigo " +
                            dtPrestamo.getCodLibro() +
                            " no esta habilitado para prestamos. No se ha podido concretar el registro.");

                int validLibroCopiaYaIngresado = (libroCopia != null) ?
                        librosPrestados.stream()
                                .filter(obj ->
                                        obj.getLibro().getLibroDetalle().getCodLibroDetalle()
                                                .equals(libroCopia.getLibroDetalle().getCodLibroDetalle()))
                                .toList().size() : 0;

                if (validLibroCopiaYaIngresado > 0)
                    throw new ResourceNotFoundException
                            ("Se ha ingresado un libro repetido. No se puede continuar con el registro");

                librosPrestados.add(new DetallePrestamo(null, null, libroCopia));
            }
        }
        else
            throw new ResourceNotFoundException
                    ("No se puede realizar el prestamo de mas de 4 libros por proceso de atención.");

        Prestamo nuevoPrestamo = new Prestamo(acService.generateCodigo("PS"), LocalDate.now(),
                null, EstadoDevolucion.DEVOLUCION_PENDIENTE, cliente);

        nuevoPrestamo = prestamoRepository.save(nuevoPrestamo);

        for (DetallePrestamo dtPrestamo : librosPrestados) {
            dtPrestamo.setPrestamo(nuevoPrestamo);
            detPrestRepository.save(dtPrestamo);
        }

        String msg = "Se ha registrado el prestamo al cliente con codigo " +
                cliente.getCodCliente() +
                ", el codigo de prestamo es " + nuevoPrestamo.getCodPrestamo() + ".";

        acService.updateTable("PS");

        return new MensajeDto<>(msg);
    }

}
