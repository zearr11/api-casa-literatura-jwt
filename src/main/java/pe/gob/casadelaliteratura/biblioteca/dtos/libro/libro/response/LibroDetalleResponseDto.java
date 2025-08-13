package pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibroDetalleResponseDto {

    private String codigo;
    private String isbn;
    private String titulo;
    private Integer year;
    private String autor;
    private String editorial;
    private String coleccion;
    private String sala;
    private Integer cantidadCopias;
    private Integer cantidadDisponibles;
    private Integer cantidadPrestados;
    private Integer cantidadSoloLectura;

}
