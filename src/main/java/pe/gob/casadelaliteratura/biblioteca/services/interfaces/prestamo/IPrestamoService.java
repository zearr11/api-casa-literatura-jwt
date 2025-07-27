package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.PrestamoRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.RangoFechasRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.request.RenovacionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.prestamo.response.PrestamoResponseDto;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.EstadoDevolucion;
import java.util.List;

public interface IPrestamoService {

    MensajeDto<String> registrarPrestamo(PrestamoRequestDto datosPrestamo);

    MensajeDto<String> renovarPrestamo(RenovacionRequestDto datosRenovacion);

    List<PrestamoResponseDto> getPrestamosPersonalizado(String codPrestamo,
                                                        String codCliente,
                                                        String codUsuario,
                                                        EstadoDevolucion estadoDevolucion,
                                                        RangoFechasRequestDto datosFecha);

}
