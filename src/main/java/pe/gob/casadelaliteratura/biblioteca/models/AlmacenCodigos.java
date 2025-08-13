package pe.gob.casadelaliteratura.biblioteca.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class AlmacenCodigos {

    @Id
    @Column(length = 3)
    private String codigo;

    @Column(nullable = false)
    private Integer numero;

}
