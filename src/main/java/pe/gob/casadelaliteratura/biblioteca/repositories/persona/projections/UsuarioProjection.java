package pe.gob.casadelaliteratura.biblioteca.repositories.persona.projections;

import java.time.LocalDate;

public interface UsuarioProjection {
    String getCodUsuario();
    String getPassword();
    String getRol();
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
