package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.models.prestamo.SancionDemora;

import java.util.List;

public interface ISancionDemoraService {
    List<SancionDemora> getAll();
    SancionDemora getById(Long id);
    SancionDemora saveOrUpdate(SancionDemora entity);
}
