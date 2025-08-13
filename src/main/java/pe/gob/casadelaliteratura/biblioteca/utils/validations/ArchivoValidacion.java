package pe.gob.casadelaliteratura.biblioteca.utils.validations;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException400;

public class ArchivoValidacion {

    public static void validacionImg(MultipartFile archivo, String nombreArchivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ErrorException400("La imagen del " + nombreArchivo +
                    " es obligatorio.");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) {
            throw new ErrorException400("La imagen del " + nombreArchivo +
                    " excede el tamaño máximo permitido de 10 MB.");
        }

        String tipo = archivo.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new ErrorException400("La imagen del " + nombreArchivo +
                    " debe ser una imagen válida.");
        }
    }

}
