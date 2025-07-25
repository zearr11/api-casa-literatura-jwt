package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.casadelaliteratura.biblioteca.models.libro.Libro;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetallePrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetallePrestamo;

    @ManyToOne
    @JoinColumn(name = "fk_cod_prestamo", nullable = false)
    private Prestamo prestamo;

    @ManyToOne
    @JoinColumn(name = "fk_cod_libro", nullable = false)
    private Libro libro;

}
