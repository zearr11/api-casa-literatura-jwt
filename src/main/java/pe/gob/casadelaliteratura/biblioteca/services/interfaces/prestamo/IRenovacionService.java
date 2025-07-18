package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Renovacion;

import java.util.List;

public interface IRenovacionService {
    List<Renovacion> getAll();
    Renovacion getById(Long id);
    Renovacion saveOrUpdate(Renovacion entity);
}
