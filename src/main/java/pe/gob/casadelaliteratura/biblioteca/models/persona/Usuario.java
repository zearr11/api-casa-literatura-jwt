package pe.gob.casadelaliteratura.biblioteca.models.persona;

import jakarta.persistence.*;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Estado;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.Role;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Usuario {

    @Id
    @Column(length = 7)
    private String codUsuario;

    @Column(nullable = false, length = 150)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @OneToOne
    @JoinColumn(name = "fk_cod_persona", nullable = false)
    private Persona persona;

}
