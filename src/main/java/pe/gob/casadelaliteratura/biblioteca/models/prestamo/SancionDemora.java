package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Cliente;

import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SancionDemora {

    @Id
    @Column(length = 7)
    private String codSancionDemora;

    @Column(nullable = false)
    private Integer diasSuspension;

    @Column(nullable = false)
    private LocalDate fechaInicioSancion;

    @Column(nullable = false)
    private LocalDate fechaFinSancion;

    @OneToOne
    @JoinColumn(name = "id_devolucion", nullable = false)
    private Devolucion devolucion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

}
