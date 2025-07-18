package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.ILibroCopiaDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.LibroCopiaData2Dto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.LibroCopiaDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.LibroDetalleDataDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.request.LibroCopiaNuevoDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.request.LibroDetalleNuevoDto;
import pe.gob.casadelaliteratura.biblioteca.models.libro.*;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.*;
import pe.gob.casadelaliteratura.biblioteca.services.impl.otros.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ILibroService;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoLibro;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class LibroService implements ILibroService {

    private final LibroRepository libRepository;
    private final LibroDetalleRepository libDetRepository;
    private final ColeccionRepository coleccionRepository;
    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;
    private final AlmacenCodigosService acService;

    public LibroService(LibroRepository libRepository, LibroDetalleRepository libDetRepository,
                        ColeccionRepository coleccionRepository, AutorRepository autorRepository,
                        EditorialRepository editorialRepository, AlmacenCodigosService acService) {

        this.libRepository = libRepository;
        this.libDetRepository = libDetRepository;
        this.coleccionRepository = coleccionRepository;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
        this.acService = acService;
    }

    @Override
    public List<LibroDetalleDataDto> getAllResumen(String codigo, String titulo, String isbn, Integer year, String autor,
                                                   String editorial, String coleccion, String sala,
                                                   Integer cantidadCopias, Integer cantidadDisponibles,
                                                   Integer cantidadPrestados, Integer cantidadSoloLectura) {

        List<LibroDetalleDataDto> lst = libRepository
                .obtenerResumenLibros(codigo, titulo, isbn, year, autor,
                        editorial, coleccion, sala, cantidadCopias,
                        cantidadDisponibles, cantidadPrestados, cantidadSoloLectura).stream()
                .map(obj -> new LibroDetalleDataDto(obj.getCodigo(),
                        obj.getIsbn(), obj.getTitulo(), obj.getYear(), obj.getAutor(), obj.getEditorial(),
                        obj.getColeccion(), obj.getSala(), obj.getCantidadCopias(), obj.getCantidadDisponibles(),
                        obj.getCantidadPrestados(), obj.getCantidadSoloLecturaEnSala()))
                .toList();

        if (lst.isEmpty())
            throw new ResourceNotFoundException("No se encontraron libros.");

        return lst;
    }

    @Override
    public List<ILibroCopiaDataDto> getAllCopias(String codigo, String isbn, String titulo,
                                                 Integer year, String autor,
                                                 String editorial, String coleccion, String sala,
                                                 Integer numeroCopia, String estado) {

        List<ILibroCopiaDataDto> lst = libRepository.obtenerCopiasLibros(codigo, isbn, titulo,
                        year, autor, editorial, numeroCopia, coleccion, sala, estado).stream()
                .map(obj -> (obj.getFechaVencimiento() != null) ?
                                new LibroCopiaDataDto(obj.getCodigo(),
                                        obj.getIsbn(), obj.getTitulo(), obj.getYear(), obj.getAutor(),
                                        obj.getEditorial(), obj.getNumeroCopia(), obj.getColeccion(),
                                        obj.getSala(), obj.getEstado(), obj.getFechaVencimiento()) :
                                new LibroCopiaData2Dto(obj.getCodigo(),
                                        obj.getIsbn(), obj.getTitulo(), obj.getYear(), obj.getAutor(),
                                        obj.getEditorial(), obj.getNumeroCopia(), obj.getColeccion(),
                                        obj.getSala(), obj.getEstado())
                        )
                .toList();

        if (lst.isEmpty())
            throw new ResourceNotFoundException("No se encontraron libros.");

        return lst;
    }

    @Override
    public MensajeDto<String> saveOrUpdateLibroDetalle(String codigo, LibroDetalleNuevoDto datosLibro) {

        LibroDetalle libroDetalle;
        String msg;
        LibroDetalle libroDetalleExistente = libDetRepository
                .findByIsbn(datosLibro.getIsbn())
                .orElse(null);

        String codColeccion = datosLibro.getCodigoColeccion();
        String codAutor = datosLibro.getCodigoAutor();
        String codEditorial = datosLibro.getCodigoEditorial();

        Coleccion coleccion = coleccionRepository.findById(codColeccion)
                        .orElse(null);
        Autor autor = autorRepository.findById(codAutor)
                        .orElse(null);
        Editorial editorial = editorialRepository.findById(codEditorial)
                        .orElse(null);

        if (coleccion == null)
            throw new ResourceNotFoundException("El codigo de coleccion ingresado no se encuentra registrado.");
        if (autor == null)
            throw new ResourceNotFoundException("El codigo de autor ingresado no se encuentra registrado.");
        if (editorial == null)
            throw new ResourceNotFoundException("El codigo de editorial ingresado no se encuentra registrado.");

        if (codigo == null) {
            if (libroDetalleExistente != null)
                throw new ResourceNotFoundException("Ya existe un libro con el isbn ingresado.");

            libroDetalle = new LibroDetalle(acService.generateCodigo("LB"), datosLibro.getIsbn(),
                    datosLibro.getTitulo(), datosLibro.getYear(), coleccion, autor, editorial);
            acService.updateTable("LB");

            msg = "Datos de libro registrado satisfactoriamente, ahora debes registrar una copia.";
        }
        else {
            if (libroDetalleExistente != null && !libroDetalleExistente.getCodLibroDetalle().equals(codigo))
                throw new ResourceNotFoundException("Ya existe un libro con el isbn ingresado.");

            libroDetalle = libDetRepository.findById(codigo)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Libro con codigo " + codigo + " no existe."));

            libroDetalle.setIsbn(datosLibro.getIsbn());
            libroDetalle.setTitulo(datosLibro.getTitulo());
            libroDetalle.setYear(datosLibro.getYear());
            libroDetalle.setColeccion(coleccion);
            libroDetalle.setAutor(autor);
            libroDetalle.setEditorial(editorial);

            msg = "Datos de libro actualizados satisfactoriamente.";
        }
        libDetRepository.save(libroDetalle);

        return new MensajeDto<>(msg);
    }

    @Override
    public MensajeDto<String> saveLibroCopia(LibroCopiaNuevoDto datosLibro) {

        LibroDetalle libroDetalle = libDetRepository
                .findById(datosLibro.getCodigoLibro())
                .orElseThrow(() -> new ResourceNotFoundException("Libro con codigo " +
                        datosLibro.getCodigoLibro() + " no existe. No se pudo crear una copia."));

        if (datosLibro.getEstadoLibro() == EstadoLibro.PRESTADO) {
            throw new ResourceNotFoundException("La copia de libro no puede registrarse como " +
                    datosLibro.getEstadoLibro());
        }

        Integer numeroCopia = libRepository.getNumeroCopiaMayor(libroDetalle.getCodLibroDetalle()) + 1;
        Libro nuevaCopiaLibro = new Libro(null, datosLibro.getEstadoLibro(), numeroCopia, libroDetalle);
        libRepository.save(nuevaCopiaLibro);

        return new MensajeDto<>("Copia de libro registrada satisfactoriamente.");
    }

}
