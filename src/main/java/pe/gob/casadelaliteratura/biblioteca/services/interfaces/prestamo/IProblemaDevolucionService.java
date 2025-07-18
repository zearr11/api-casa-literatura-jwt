package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.ProblemaDevolucion;

import java.util.List;

public interface IProblemaDevolucionService {
    List<ProblemaDevolucion> getAll();
    ProblemaDevolucion getById(Long id);
    ProblemaDevolucion saveOrUpdate(ProblemaDevolucion entity);
}
