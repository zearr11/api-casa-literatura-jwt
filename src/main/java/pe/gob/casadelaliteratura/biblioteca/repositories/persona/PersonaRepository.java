package pe.gob.casadelaliteratura.biblioteca.repositories.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.casadelaliteratura.biblioteca.models.persona.Persona;

public interface PersonaRepository extends JpaRepository<Persona, String> {}
