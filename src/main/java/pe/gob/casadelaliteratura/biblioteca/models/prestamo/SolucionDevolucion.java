package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SolucionDevolucion {

    @Id
    @Column(length = 7)
    private String codSolucionDev;

    @Column(nullable = false)
    private LocalDate fechaSolucion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String detalle;

    @OneToOne
    @JoinColumn(name = "id_problema_devolucion", nullable = false)
    private ProblemaDevolucion problemaDevolucion;

}
