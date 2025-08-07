package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SancionDemora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codSancionDemora;

    @Column(nullable = false)
    private Integer diasSuspension;

    @Column(nullable = false)
    private LocalDate fechaInicioSancion;

    @Column(nullable = false)
    private LocalDate fechaFinSancion;

    @OneToOne
    @JoinColumn(name = "fk_cod_devolucion", nullable = false)
    private Devolucion devolucion;

}
