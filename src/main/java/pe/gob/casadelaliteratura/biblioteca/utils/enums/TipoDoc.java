package pe.gob.casadelaliteratura.biblioteca.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoDoc {
    DNI, CE;

    /*
    @JsonCreator
    public static TipoDoc fromString(String value) {
        return TipoDoc.valueOf(value.toUpperCase());
    }
    */
}
