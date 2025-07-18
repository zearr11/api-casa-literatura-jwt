package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetalleDevolucion;

import java.util.List;

public interface IDetalleDevolucionService {
    List<DetalleDevolucion> getAll();
    DetalleDevolucion getById(Long id);
    DetalleDevolucion saveOrUpdate(DetalleDevolucion entity);
}
