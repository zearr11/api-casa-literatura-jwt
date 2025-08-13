package pe.gob.casadelaliteratura.biblioteca.services.impl;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.models.AlmacenCodigos;
import pe.gob.casadelaliteratura.biblioteca.repositories.AlmacenCodigosRepository;

@Service
public class AlmacenCodigosService {

    private final AlmacenCodigosRepository acRepository;

    public AlmacenCodigosService(AlmacenCodigosRepository acRepository) {
        this.acRepository = acRepository;
    }

    /*
        PA -> Persona
        US -> Usuario
        CL -> Cliente
        SL -> Sala
        CC -> Coleccion
        AT -> Autor
        ED -> Editorial
        LB -> LibroDetalle
        DV -> Devolucion
        PS -> Prestamo
        PD -> ProblemaDevolucion
        RN -> Renovacion X
        SR -> SancionDemora X
        SD -> SolucionDevolucion
    */
    public String generateCodigo(String prefijo) {
        return prefijo + String.format("%05d", getLastIdByTable(prefijo));
    }

    // Actualiza el ultimo id almacenado en la tabla
    public void updateTable(String prefijo) {
        AlmacenCodigos ac = acRepository.findById(prefijo).orElse(null);
        if (ac != null){
            ac.setNumero(ac.getNumero() + 1);
            acRepository.save(ac);
        }
    }

    // Obtiene el ultimo id almacenado en la tabla de id's
    private Integer getLastIdByTable(String prefijo) {
        AlmacenCodigos ac = acRepository.findById(prefijo).orElse(null);
        return (ac != null) ? ac.getNumero() : null;
    }

}
