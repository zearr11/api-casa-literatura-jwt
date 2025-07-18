package pe.gob.casadelaliteratura.biblioteca.models.cliente;

import jakarta.persistence.*;
import lombok.*;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoDoc;

import java.time.LocalDate;

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

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false, length = 9)
    private String numeroPrincipal;

    @Column(nullable = false, length = 9)
    private String numeroSecundario;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDoc tipoDoc;

    @Column(nullable = false, length = 20, unique = true)
    private String numeroDoc;

    @OneToOne
    @JoinColumn(name = "id_documentacion", nullable = false)
    private Documentacion documentacion;

}
