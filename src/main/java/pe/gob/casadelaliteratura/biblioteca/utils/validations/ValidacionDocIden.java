package pe.gob.casadelaliteratura.biblioteca.utils.validations;

import pe.gob.casadelaliteratura.biblioteca.utils.enums.TipoDoc;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException400;

public class ValidacionDocIden {

    public static void validationDoc(String numDoc, TipoDoc tipoDoc) {

        boolean esDocValido = false;

        if (tipoDoc.equals(TipoDoc.DNI) && numDoc.length()==8)
            esDocValido = true;
        else if (tipoDoc.equals(TipoDoc.CE) &&
                (numDoc.length()>=8 && numDoc.length()<=13))
            esDocValido = true;

        if (!esDocValido)
            throw new ErrorException400("El numero de documento ingresado, no es válido.");
    }

}
