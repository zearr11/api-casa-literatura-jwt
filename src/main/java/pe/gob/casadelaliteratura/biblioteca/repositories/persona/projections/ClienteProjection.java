package pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections;

import java.time.LocalDate;

public interface ClienteProjection {
    String getCodCliente();
    String getNumeroSecundario();
    String getUrlDocIdentidad();
    String getUrlRecServicio();
    String getCodPersona();
    String getApellidos();
    String getCorreo();
    String getDireccion();
    LocalDate getFechaNacimiento();
    String getNombres();
    String getNumeroDoc();
    String getNumeroPrincipal();
    String getTipoDoc();
}
