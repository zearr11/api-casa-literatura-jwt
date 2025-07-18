package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.Devolucion;

import java.util.List;

public interface IDevolucionService {
    List<Devolucion> getAll();
    Devolucion getById(Long id);
    Devolucion saveOrUpdate(Devolucion entity);
}
