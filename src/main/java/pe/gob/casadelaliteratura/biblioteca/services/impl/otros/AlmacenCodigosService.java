package pe.gob.casadelaliteratura.biblioteca.services.impl.otros;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.models.otros.AlmacenCodigos;
import pe.gob.casadelaliteratura.biblioteca.repositories.otros.AlmacenCodigosRepository;

@Service
public class AlmacenCodigosService {

    private final AlmacenCodigosRepository acRepository;

    public AlmacenCodigosService(AlmacenCodigosRepository acRepository) {
        this.acRepository = acRepository;
    }

    /*
        CL -> Cliente
        SL -> Sala
        CC -> Coleccion
        AT -> Autor
        ED -> Editorial
        LB -> LibroDetalle
        DV -> Devolucion
        PS -> Prestamo
        PD -> ProblemaDevolucion
        RN -> Renovacion
        SR -> SancionDemora
        SD -> SolucionDevolucion
    */
    public String generateCodigo(String prefijo) {
        return prefijo + String.format("%05d", getLastIdByTable(prefijo));
    }

    public Integer getLastIdByTable(String prefijo) {
        AlmacenCodigos ac = acRepository.findById(prefijo).orElse(null);
        return (ac != null) ? ac.getNumero() : null;
    }

    public void updateTable(String prefijo) {
        AlmacenCodigos ac = acRepository.findById(prefijo).orElse(null);
        if (ac != null){
            ac.setNumero(ac.getNumero()+1);
            acRepository.save(ac);
        }
    }

}
