package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoEntrega;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetalleDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipoEntrega;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_devolucion")
    private Devolucion devolucion;

    @OneToOne
    @JoinColumn(nullable = false, name = "id_detalle_prestamo")
    private DetallePrestamo detallePrestamo;

}
