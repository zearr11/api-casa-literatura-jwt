package pe.gob.casadelaliteratura.biblioteca.models.cliente;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Documentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumentacion;

    @Column(nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte[] imgDocIdentidad;

    @Column(nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte[] imgRecServicio;

}
