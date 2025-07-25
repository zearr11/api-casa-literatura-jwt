package pe.gob.casadelaliteratura.biblioteca.models.prestamo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Usuario;
import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Devolucion {

    @Id
    @Column(length = 7)
    private String codDevolucion;

    @Column(nullable = false)
    private LocalDate fechaDevolucion;

    @OneToOne
    @JoinColumn(name = "fk_cod_prestamo", nullable = false)
    private Prestamo prestamo;

    @ManyToOne
    @JoinColumn(name = "fk_cod_usuario", nullable = false)
    private Usuario usuario;

}
