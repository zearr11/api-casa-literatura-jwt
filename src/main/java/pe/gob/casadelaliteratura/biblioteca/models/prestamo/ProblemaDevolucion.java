package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoProblema;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoProblema;

import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProblemaDevolucion {

    @Id
    @Column(length = 7)
    private String codProblemaDevolucion;

    @Column(nullable = false)
    private LocalDate fechaProblema;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String detalle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProblema tipoProblema;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProblema estadoProblema;

    @OneToOne
    @JoinColumn(name = "id_detalle_devolucion", nullable = false)
    private DetalleDevolucion detalleDevolucion;

}
