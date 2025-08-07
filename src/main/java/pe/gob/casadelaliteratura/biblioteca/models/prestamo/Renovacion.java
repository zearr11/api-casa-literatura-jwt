package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.MedioSolicitud;
import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Renovacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codRenovacion;

    @Column(nullable = false)
    private LocalDate fechaSolicitud;

    @Column(nullable = false)
    private LocalDate nuevaFechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedioSolicitud medioSolicitud;

    @ManyToOne
    @JoinColumn(name = "fk_cod_prestamo", nullable = false)
    private Prestamo prestamo;

    @ManyToOne
    @JoinColumn(name = "fk_cod_usuario", nullable = false)
    private Usuario usuario;

}
