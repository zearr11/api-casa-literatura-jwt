package pe.gob.casadelaliteratura.biblioteca.services.interfaces.prestamo;

import pe.gob.casadelaliteratura.biblioteca.dtos.MensajeDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.DevolucionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.dtos.prestamo.devolucion.SolucionRequestDto;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.DetalleDevolucion;
import pe.gob.casadelaliteratura.biblioteca.models.prestamo.ProblemaDevolucion;
import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoProblema;

public interface IDevolucionService {

    MensajeDto<String> registrarDevolucion(DevolucionRequestDto datosDevolucion);

    MensajeDto<String> registrarSolucionDevolucion(SolucionRequestDto datosSolucion);

    void procesarProblemaDevolucion(DetalleDevolucion datosDetalleDevolucion,
                                    String detalleProblema, TipoProblema tipoProblema);

    void procesarSolucionDevolucion(ProblemaDevolucion datosProblemaDevolucion,
                                                  String detalleSolucion);

}
