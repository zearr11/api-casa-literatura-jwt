package pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors;

import lombok.Getter;

@Getter
public class Error2Exception400 extends RuntimeException {

    private final String detalle;

    public Error2Exception400(String mensaje, String detalle) {
        super(mensaje);
        this.detalle = detalle;
    }

}
