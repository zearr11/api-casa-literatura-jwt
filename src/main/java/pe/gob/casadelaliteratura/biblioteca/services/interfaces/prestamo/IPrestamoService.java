package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.PrestamoRequestDto;

public interface IPrestamoService {
    //List<Prestamo> getAll();
    //Prestamo getById(Long id);
    MensajeDto<String> registrar(PrestamoRequestDto datosPrestamo);
}
