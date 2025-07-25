package pe.gob.casadelaliteratura.biblioteca.models.libro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LibroDetalle {

    @Id
    @Column(length = 7)
    private String codLibroDetalle;

    @Column(nullable = false, length = 50, unique = true)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "fk_cod_coleccion", nullable = false)
    private Coleccion coleccion;

    @ManyToOne
    @JoinColumn(name = "fk_cod_autor", nullable = false)
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "fk_cod_editorial", nullable = false)
    private Editorial editorial;

}
