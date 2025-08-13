package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;

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
    @JoinColumn(name = "fk_cod_problema_devolucion", nullable = false)
    private ProblemaDevolucion problemaDevolucion;

    @ManyToOne
    @JoinColumn(name = "fk_cod_usuario", nullable = false)
    private Usuario usuario;

}
