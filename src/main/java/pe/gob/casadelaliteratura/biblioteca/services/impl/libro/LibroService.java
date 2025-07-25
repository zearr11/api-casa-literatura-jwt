package pe.gob.casadelaliteratura.biblioteca.services.impl.libro;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request.LibroDetalleNuevoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.request.LibroNuevaCopiaRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.LibroDetalleResponseDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.copias.ILibroCopiaResponse;
import pe.gob.casadelaliteratura.biblioteca.models.libro.*;
import pe.gob.casadelaliteratura.biblioteca.repositories.libro.*;
import pe.gob.casadelaliteratura.biblioteca.services.impl.AlmacenCodigosService;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.libro.ILibroService;
import pe.gob.casadelaliteratura.biblioteca.utils.converts.LibroConvert;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoLibro;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException400;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException404;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.util.List;

@Service
public class LibroService implements ILibroService {

    private final LibroRepository libRepository;
    private final LibroDetalleRepository libDetRepository;
    private final ColeccionRepository coleccionRepository;
    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;
    private final AlmacenCodigosService acService;

    public LibroService(LibroRepository libRepository,
                        LibroDetalleRepository libDetRepository,
                        ColeccionRepository coleccionRepository,
                        AutorRepository autorRepository,
                        EditorialRepository editorialRepository,
                        AlmacenCodigosService acService) {
        this.libRepository = libRepository;
        this.libDetRepository = libDetRepository;
        this.coleccionRepository = coleccionRepository;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
        this.acService = acService;
    }

    @Transactional
    @Override
    public MensajeDto<String> saveLibroCopia(LibroNuevaCopiaRequestDto datosLibro) {

        LibroDetalle libroDetalle = libDetRepository
                .findById(datosLibro.getCodigoLibro())
                .orElseThrow(() -> new ErrorException404("No se encontró el libro con el codigo: " +
                        datosLibro.getCodigoLibro() + ". No se pudo crear una copia."));

        if (datosLibro.getEstado() == EstadoLibro.PRESTADO)
            throw new ErrorException400("La copia de libro no puede registrarse como "
                    + datosLibro.getEstado());

        Integer numeroCopia = libRepository.getNumeroCopiaMayor(libroDetalle.getCodLibroDetalle()) + 1;
        Libro nuevaCopiaLibro = new Libro(null, datosLibro.getEstado(), numeroCopia, libroDetalle);
        libRepository.save(nuevaCopiaLibro);

        return new MensajeDto<>("Copia de libro registrada satisfactoriamente.");

    }

    @Transactional
    @Override
    public MensajeDto<String> saveOrUpdateLibroDetalle(String codigo,
                                                       LibroDetalleNuevoRequestDto datosLibro) {
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
            throw new ErrorException404("El codigo de coleccion ingresado no se encuentra registrado.");
        if (autor == null)
            throw new ErrorException404("El codigo de autor ingresado no se encuentra registrado.");
        if (editorial == null)
            throw new ErrorException404("El codigo de editorial ingresado no se encuentra registrado.");

        if (codigo == null) {
            if (libroDetalleExistente != null)
                throw new ErrorException409("Ya existe un libro con el isbn ingresado.");

            libroDetalle = new LibroDetalle(acService.generateCodigo("LB"), datosLibro.getIsbn(),
                    datosLibro.getTitulo(), datosLibro.getYear(), coleccion, autor, editorial);
            msg = "Datos de libro registrado satisfactoriamente, ahora debes registrar una copia.";
        }
        else {
            if (libroDetalleExistente != null && !libroDetalleExistente.getCodLibroDetalle().equals(codigo))
                throw new ErrorException409("Ya existe un libro con el isbn ingresado.");

            libroDetalle = libDetRepository.findById(codigo)
                    .orElseThrow(() ->
                            new ErrorException409(
                                    "No se encontró el libro con el codigo: " + codigo
                            ));

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
    public List<LibroDetalleResponseDto> getAllResumen(String codigo, String titulo,
                                                       String isbn, Integer year,
                                                       String autor, String editorial,
                                                       String coleccion, String sala,
                                                       Integer cantidadCopias, Integer cantidadDisponibles,
                                                       Integer cantidadPrestados, Integer cantidadSoloLectura) {
        List<LibroDetalleResponseDto> lst = libRepository
                .obtenerResumenLibros(codigo, titulo, isbn, year, autor,
                        editorial, coleccion, sala, cantidadCopias,
                        cantidadDisponibles, cantidadPrestados, cantidadSoloLectura).stream()
                .map(LibroConvert::libroResumenProjectionToLibroDetalleResponseDto)
                .toList();

        if (lst.isEmpty())
            throw new ErrorException404("No se encontraron libros.");

        return lst;
    }

    @Override
    public List<ILibroCopiaResponse> getAllCopias(String codigo, String isbn,
                                                  String titulo, Integer year,
                                                  String autor, String editorial,
                                                  String coleccion, String sala,
                                                  Integer numeroCopia, String estado) {
        List<ILibroCopiaResponse>
                lst = libRepository.obtenerCopiasLibros(codigo, isbn, titulo,
                        year, autor, editorial, numeroCopia, coleccion, sala, estado).stream()
                .map(obj -> (obj.getFechaVencimiento() != null) ?
                         LibroConvert.libroCopiasProjectionToLibroCopiaResponseDto(obj) :
                         LibroConvert.libroCopiasProjectionToLibroCopiaResponseDto2(obj)
                ).toList();

        if (lst.isEmpty())
            throw new ErrorException404("No se encontraron libros.");

        return lst;
    }

}
