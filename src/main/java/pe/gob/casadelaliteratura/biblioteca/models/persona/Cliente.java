package pe.gob.casadelaliteratura.biblioteca.models.persona;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cliente {

    @Id
    @Column(length = 7)
    private String codCliente;

    @Column(nullable = false, length = 150)
    private String urlDocIdentidad;

    @Column(nullable = false, length = 150)
    private String urlRecServicio;

    @Column(length = 9)
    private String numeroSecundario;

    @OneToOne
    @JoinColumn(name = "fk_cod_persona", nullable = false)
    private Persona persona;

}
