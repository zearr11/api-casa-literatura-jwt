package pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class  LibroDetalleNuevoDto {

    private String isbn;
    private String titulo;
    private Integer year;
    private String codigoAutor;
    private String codigoColeccion;
    private String codigoEditorial;

}
