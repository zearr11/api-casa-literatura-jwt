package pe.gob.casadelaliteratura.biblioteca.dtos.libro.libro.response.copias;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibroCopiaResponseDto2 implements ILibroCopiaResponse {

    private String codigo;
    private String isbn;
    private String titulo;
    private Integer year;
    private String autor;
    private String editorial;
    private Integer numeroCopia;
    private String coleccion;
    private String sala;
    private String estado;

}
