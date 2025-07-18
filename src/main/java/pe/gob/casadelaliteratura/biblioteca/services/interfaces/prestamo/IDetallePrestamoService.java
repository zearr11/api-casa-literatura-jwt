package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetallePrestamo;

import java.util.List;

public interface IDetallePrestamoService {
    List<DetallePrestamo> getAll();
    DetallePrestamo getById(Long id);
    DetallePrestamo saveOrUpdate(DetallePrestamo entity);
}
