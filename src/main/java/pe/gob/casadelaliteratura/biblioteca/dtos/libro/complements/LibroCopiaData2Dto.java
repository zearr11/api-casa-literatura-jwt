package pe.gob.casadelaliteratura.biblioteca.dtos.libro.complements;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibroCopiaData2Dto implements ILibroCopiaDataDto {

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
