package pe.gob.casadelaliteratura.biblioteca.services.impl.prestamo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.DevolucionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.SolucionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements.DetalleEntregaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements.ProblemaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.complements.SolucionLibrosDetalleRequestDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Libro;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.*;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.LibroRepository;
import pe.gob.casadelaliteratura.biblioteca.repositories.prestamo.*;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.impl.auth.AuthService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo.IDevolucionService;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.*;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.time.LocalDate;
import java.util.List;

@Service
public class DevolucionService implements IDevolucionService {

    private static final String MSG_DEVOLUCION_COMPLETA = "Devolución completa registrada satisfactoriamente.";
    private static final String MSG_DEVOLUCION_PARCIAL = "Devolución parcial registrada satisfactoriamente.";

    private final DevolucionRepository devolucionRepository;
    private final PrestamoRepository prestamoRepository;
    private final DetallePrestamoRepository detallePrestRepository;
    private final DetalleDevolucionRepository detalleDevRepository;
    private final AlmacenCodigosService acService;
    private final AuthService authService;
    private final LibroRepository libroRepository;
    private final ProblemaDevolucionRepository problemaRepository;
    private final SolucionDevolucionRepository solucionDevolucionRepository;

    public DevolucionService(DevolucionRepository devolucionRepository,
                             PrestamoRepository prestamoRepository,
                             DetallePrestamoRepository detallePrestRepository,
                             DetalleDevolucionRepository detalleDevRepository,
                             AlmacenCodigosService acService, AuthService authService,
                             LibroRepository libroRepository,
                             ProblemaDevolucionRepository problemaRepository,
                             SolucionDevolucionRepository solucionDevolucionRepository) {
        this.devolucionRepository = devolucionRepository;
        this.prestamoRepository = prestamoRepository;
        this.detallePrestRepository = detallePrestRepository;
        this.detalleDevRepository = detalleDevRepository;
        this.acService = acService;
        this.authService = authService;
        this.libroRepository = libroRepository;
        this.problemaRepository = problemaRepository;
        this.solucionDevolucionRepository = solucionDevolucionRepository;
    }

    @Transactional
    @Override
    public MensajeDto<String> registrarDevolucion(DevolucionRequestDto datosDevolucion) {

        // Mensaje final
        String msg;

        // Fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Codigo del prestamo
        String codPrestamo = datosDevolucion.getCodigoPrestamo();

        // Busqueda del prestamo en la base de datos
        Prestamo prestamo = prestamoRepository.findById(codPrestamo)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró el préstamo con el código: " + codPrestamo +
                                ". No se puede continuar con la solicitud."
                ));

        if (prestamo.getEstadoDevolucion().equals(EstadoDevolucion.DEVOLUCION_COMPLETA)) {
            throw new ErrorException409(
                    "El préstamo con código: " + codPrestamo + ", ya se encuentra con un estado de devolución completa. " +
                            "No se puede continuar con la solicitud."
            );
        }

        if (prestamo.getEstadoDevolucion() == EstadoDevolucion.DEVOLUCION_PARCIAL) {
            throw new ErrorException409(
                    "El préstamo con código: " + codPrestamo + ", actualmente tiene un estado de devolución parcial. " +
                            "Debe registrar la solución para completar la devolución. No se puede continuar con la solicitud."
            );
        }

        // Detalle de libros prestados en la base de datos
        List<DetallePrestamo> librosPrestados = detallePrestRepository.findByPrestamo(prestamo);
        // Detalle de libros devueltos en la solicitud
        List<DetalleEntregaRequestDto> librosDevueltos = datosDevolucion.getDetalleLibroDevolucion();
        // Contador de libros entregados
        int cantidadLibrosEntregados = 0;

        // Validación: Si libros prestados es diferente a libros devueltos
        if (librosPrestados.size() != librosDevueltos.size())
            throw new ErrorException409(
                    "La cantidad de libros devueltos no coincide con la cantidad de libros prestados. " +
                            "No se puede continuar con la solicitud."
            );

        if (devolucionRepository.findByPrestamo(prestamo).isPresent())
            throw new ErrorException409(
                    "Ya existe una devolución registrada para el préstamo con código: " + codPrestamo +
                            ". No se puede continuar con la solicitud."
            );

        // Registrar devolución general
        Devolucion devolucion = devolucionRepository.save(new Devolucion(acService.generateCodigo("DV"),
                fechaActual, prestamo, authService.obtenerUsuarioAutenticado()));
        acService.updateTable("DV");


        // Iterar sobre los libros devueltos de la solicitud
        for (DetalleEntregaRequestDto libroDevuelto : librosDevueltos) {

             if (libroDevuelto.getTipoEntrega().equals(TipoEntrega.ENTREGADO) &&
                     libroDevuelto.getDetalleProblema() != null)
                 throw new ErrorException409(
                         "El libro con codigo: " + libroDevuelto.getCodigoLibro() + " y numero de copia: " +
                                 libroDevuelto.getNumeroCopia() +
                                 " se marco como entregado, pero se incluyo un problema. " +
                                 "Para registrar un problema se debe marcar el libro como no entregado. " +
                                 "No se puede continuar con la solicitud."
                 );

            // Inicializar obj detalle de libros prestados
            DetallePrestamo detallePrestamo = new DetallePrestamo();

            // Codigo de libro devuelto
            String codLibroDevuelto = libroDevuelto.getCodigoLibro();
            // Numero de copia devuelto
            Integer numeroCopiaDevuelto = libroDevuelto.getNumeroCopia();

            // Busqueda del libro devuelto en el detalle de libros prestados
            for (DetallePrestamo detallePrest : librosPrestados) {

                // Codigo y numero de copia del libro prestado
                String codLibroPrestado = detallePrest.getLibro().getLibroDetalle()
                        .getCodLibroDetalle();
                Integer numeroCopiaPrestada = detallePrest.getLibro().getNumeroCopia();

                // Validación: Si el libro devuelto coincide con el libro prestado
                // asignar al obj detallePrestamo y salir del for de libros prestados
                if (codLibroDevuelto.equals(codLibroPrestado) &&
                        numeroCopiaDevuelto.equals(numeroCopiaPrestada)) {
                    detallePrestamo = detallePrest;
                    break;
                }

                // Si no coincide asignar null
                detallePrestamo = null;
            }

            // Validación: Si detallePrestamo es null, significa que no se encontró
            // el libro devuelto en el detalle de libros prestados
            if (detallePrestamo == null)
                throw new ErrorException409("El libro devuelto con código: " + codLibroDevuelto +
                        " no se ha encontrado en el préstamo con código: " + codPrestamo +
                        ". No se puede continuar con la solicitud.");

            // Crear obj detalle de devolución para guardar en la base de datos
            DetalleDevolucion detalleToSave = new DetalleDevolucion(null,
                    libroDevuelto.getTipoEntrega(), devolucion, detallePrestamo);

            // Guardar el detalle de devolución
            detalleToSave = detalleDevRepository.save(detalleToSave);

            // Obtener los detalles del problema si se asignaron valores
            ProblemaRequestDto datosProblema = libroDevuelto.getDetalleProblema();

            // Validacion: Si el detalle de devolucion guardado es NO ENTREGADO
            if (detalleToSave.getTipoEntrega().equals(TipoEntrega.NO_ENTREGADO)) {

                // Lanzar excepcion si no se proporcionaron los detalles
                // de problema al indicar en el detalle de devolución
                // que el libro no fue entregado
                if (datosProblema == null)
                    throw new ErrorException409(
                            "Se indico el tipo de entrega como 'NO ENTREGADO' en el libro devuelto con código: " +
                                    codLibroDevuelto + ". Pero no se ha proporcionado el detalle del problema." +
                                    " No se puede continuar con la solicitud."
                    );

                // Ejecutar el proceso para registrar el problema
                procesarProblemaDevolucion(detalleToSave,
                        datosProblema.getDetalleProblema(), datosProblema.getTipoProblema());

            }

            // Validacion: Si el detalle de devolucion guardado es ENTREGADO
            if (detalleToSave.getTipoEntrega().equals(TipoEntrega.ENTREGADO)) {

                // Obtener el libro que fue entregado y actualizar su estado a DISPONIBLE
                Libro libroToUpdate = detalleToSave.getDetallePrestamo().getLibro();
                libroToUpdate.setEstado(EstadoLibro.DISPONIBLE);
                // Actualizar el libro
                libroRepository.save(libroToUpdate);
                // Incrementar el contador de libros entregados
                cantidadLibrosEntregados += 1;

            }

            // Fin del for para recorrer los libros devueltos
        }

        // Validacion: Si la cantidad de libros entregados es igual a la cantidad
        // de libros prestados, entonces el prestamo se marca como completo
        if (cantidadLibrosEntregados == librosPrestados.size()){
            prestamo.setEstadoDevolucion(EstadoDevolucion.DEVOLUCION_COMPLETA);
            msg = MSG_DEVOLUCION_COMPLETA;
        }
        // Sino se marca como devolucion parcial
        else {
            prestamo.setEstadoDevolucion(EstadoDevolucion.DEVOLUCION_PARCIAL);
            msg = MSG_DEVOLUCION_PARCIAL;
        }

        // Actualiza el prestamo
        prestamoRepository.save(prestamo);

        return new MensajeDto<>(msg);
    }

    @Transactional
    @Override
    public MensajeDto<String> registrarSolucionDevolucion(SolucionRequestDto datosSolucion) {

        // Asignacion de codigo de prestamo a variable y busqueda en la bd
        String codPrestamo = datosSolucion.getCodigoPrestamo();
        Prestamo prestamo = prestamoRepository.findById(codPrestamo)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontró el préstamo con el código: " + codPrestamo +
                                ". No se puede continuar con la solicitud."
                ));

        if (prestamo.getEstadoDevolucion().equals(EstadoDevolucion.DEVOLUCION_COMPLETA)) {
            throw new ErrorException409(
                    "El préstamo con código: " + codPrestamo + ", ya se encuentra con un estado de devolución completa. " +
                            "No se puede continuar con la solicitud."
            );
        }

        if (prestamo.getEstadoDevolucion() == EstadoDevolucion.DEVOLUCION_PENDIENTE) {
            throw new ErrorException409(
                    "El préstamo con código: " + codPrestamo + ", aun no ha tiene registrado una devolución. " +
                            "No se puede continuar con la solicitud."
            );
        }

        // Busqueda de problemas asociados al prestamo
        List<String> codsProblemasDev = problemaRepository.findByPrestamo(codPrestamo)
                .orElseThrow(() -> new ErrorException404(
                        "No se encontraron problemas registrados para el préstamo con código: " + codPrestamo +
                                ". No se puede continuar con la solicitud."
                ));

        // Asignacion del detalle de solucion del request a una lista
        List<SolucionLibrosDetalleRequestDto> detalleSolucionRequest = datosSolucion.getDetalleSolucion();

        int cantidadLibrosSolucionadosAhora = 0;
        int problemasYaSolucionadosAnteriores = 0;

        for (String codProblema : codsProblemasDev) {
            ProblemaDevolucion problema = problemaRepository.findById(codProblema).get();
            if (problema.getEstadoProblema().equals(EstadoProblema.LIBRO_REPUESTO))
                problemasYaSolucionadosAnteriores += 1;
        }

        for (SolucionLibrosDetalleRequestDto requestData : detalleSolucionRequest) {

            ProblemaDevolucion objProblemaBd = new ProblemaDevolucion();

            String codLibroRequest = requestData.getCodigoLibro();
            Integer numCopiaRequest = requestData.getNumeroCopia();

            for (String codProblema : codsProblemasDev) {

                // Busqueda del problema asociado al codigo de problema en la bd
                ProblemaDevolucion problema = problemaRepository.findById(codProblema).get();

                // Asignacion del codigo y numero de copia del libro encontrados en la bd
                String codLibroProblema = problema.getDetalleDevolucion().getDetallePrestamo()
                        .getLibro().getLibroDetalle().getCodLibroDetalle();
                Integer numCopiaLibroProblema = problema.getDetalleDevolucion().getDetallePrestamo()
                        .getLibro().getNumeroCopia();

                if (codLibroProblema.equals(codLibroRequest) &&
                        numCopiaLibroProblema.equals(numCopiaRequest)) {
                    objProblemaBd = problema;
                    break;
                }

                objProblemaBd = null;
            }

            SolucionDevolucion datosSolucionDev = solucionDevolucionRepository
                    .findByProblemaDevolucion(objProblemaBd).orElse(null);

            if (datosSolucionDev != null)
                throw new ErrorException409(
                        "El libro: " + codLibroRequest + ", con el numero de copia: " + numCopiaRequest +
                                " ya tiene una solución registrada. " +
                                "No se puede continuar con la solicitud."
                );

            if (objProblemaBd == null)
                throw new ErrorException409(
                        "El codigo de libro: " + codLibroRequest + " con el numero de copia: " + numCopiaRequest +
                                " no esta asociado a ningún problema del préstamo ingresado. " +
                                "No se puede continuar con la solicitud."
                );

            procesarSolucionDevolucion(objProblemaBd, requestData.getDetalleSolucion());
            cantidadLibrosSolucionadosAhora += 1;
        }

        String msg;

        if ((cantidadLibrosSolucionadosAhora + problemasYaSolucionadosAnteriores) ==
                codsProblemasDev.size()) {
            prestamo.setEstadoDevolucion(EstadoDevolucion.DEVOLUCION_COMPLETA);
            msg = "Se ha registrado la devolución completa del préstamo.";
        }
        else {
            msg = "Se ha procesado correctamente la solución de los libros indicados. " +
                    "Sin embargo, el préstamo aun tiene problemas pendientes de solucionar. ";
        }
        prestamoRepository.save(prestamo);

        return new MensajeDto<>(msg);
    }

    @Transactional
    @Override
    public void procesarProblemaDevolucion(DetalleDevolucion datosDetalleDevolucion,
                                           String detalleProblema, TipoProblema tipoProblema) {

        ProblemaDevolucion problema = new ProblemaDevolucion(acService.generateCodigo("PD"),
                LocalDate.now(), detalleProblema, tipoProblema,
                EstadoProblema.PENDIENTE, datosDetalleDevolucion);
        problemaRepository.save(problema);
        acService.updateTable("PD");
    }

    @Transactional
    @Override
    public void procesarSolucionDevolucion(ProblemaDevolucion datosProblemaDevolucion,
                                           String detalleSolucion) {

        // Actualizar el estado del problema a libro repuesto
        datosProblemaDevolucion.setEstadoProblema(EstadoProblema.LIBRO_REPUESTO);
        datosProblemaDevolucion = problemaRepository.save(datosProblemaDevolucion);

        // Actualizar libro a DISPONIBLE
        Libro libroRepuesto = datosProblemaDevolucion.getDetalleDevolucion().getDetallePrestamo().getLibro();
        libroRepuesto.setEstado(EstadoLibro.DISPONIBLE);
        libroRepository.save(libroRepuesto);

        // Actualizar el estado del detalle devolucion a ENTREGADO
        DetalleDevolucion detDev = datosProblemaDevolucion.getDetalleDevolucion();
        detDev.setTipoEntrega(TipoEntrega.ENTREGADO);
        detalleDevRepository.save(detDev);

        // Guardar la solución
        SolucionDevolucion solucion = new SolucionDevolucion(acService.generateCodigo("SD"),
                LocalDate.now(), detalleSolucion,
                datosProblemaDevolucion, authService.obtenerUsuarioAutenticado());
        solucionDevolucionRepository.save(solucion);
        acService.updateTable("SD");

    }

}
