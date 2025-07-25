package pe.gob.casadelaliteratura.biblioteca.models.libro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coleccion {

    @Id
    @Column(length = 7)
    private String codColeccion;

    @Column(nullable = false, length = 100, unique = true)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "fk_cod_sala", nullable = false)
    private Sala sala;

}
