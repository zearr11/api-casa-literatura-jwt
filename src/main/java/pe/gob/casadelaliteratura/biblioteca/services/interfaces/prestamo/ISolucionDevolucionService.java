package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.SolucionDevolucion;

import java.util.List;

public interface ISolucionDevolucionService {
    List<SolucionDevolucion> getAll();
    SolucionDevolucion getById(Long id);
    SolucionDevolucion saveOrUpdate(SolucionDevolucion entity);
}
