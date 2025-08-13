package pe.gob.casadelaliteratura.biblioteca.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException400;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.errors.ErrorException409;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final RestTemplate restTemplate;

    public CloudinaryService(Cloudinary cloudinary, RestTemplate restTemplate) {
        this.cloudinary = cloudinary;
        this.restTemplate = restTemplate;
    }

    public String subirArchivoImg(MultipartFile archivo) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(archivo.getBytes(), Map.of("folder", "biblioteca"));

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new ErrorException400("Error al subir archivo a Cloudinary");
        }
    }

    public boolean sonImagenesIguales(MultipartFile nuevaImagen, String urlImagenCloudinary) {
        try {
            byte[] imagenActualBytes = restTemplate.getForObject(urlImagenCloudinary, byte[].class);
            byte[] nuevaImagenBytes = nuevaImagen.getBytes();

            String hashActual = DigestUtils.sha256Hex(imagenActualBytes);
            String hashNueva = DigestUtils.sha256Hex(nuevaImagenBytes);

            return hashActual.equals(hashNueva);
        }
        catch (Exception e) {
            return false;
        }
    }

    public void eliminarArchivo(String url) {
        try {
            String publicId = extraerPublicIdDesdeUrl(url);
            System.out.println("Public ID extraído: " + publicId);

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Resultado Cloudinary: " + result);

            if (!"ok".equals(result.get("result"))) {
                throw new ErrorException409("No se pudo eliminar la imagen de Cloudinary.");
            }
        }
        catch (Exception e) {
            throw new ErrorException409(e.getMessage());
        }
    }

    private String extraerPublicIdDesdeUrl(String url) {
        try {
            String[] partes = url.split("/");

            StringBuilder publicIdBuilder = new StringBuilder();
            boolean comenzar = false;

            for (int i = 0; i < partes.length; i++) {
                String parte = partes[i];

                if (comenzar) {
                    // Saltar la parte de versión si comienza con 'v' seguido de dígitos
                    if (parte.matches("^v\\d+$")) {
                        continue;
                    }

                    // Última parte: el archivo con extensión (ej. DNI.jpg)
                    if (i == partes.length - 1) {
                        int punto = parte.lastIndexOf('.');
                        parte = (punto != -1) ? parte.substring(0, punto) : parte;
                        publicIdBuilder.append(parte);
                    } else {
                        publicIdBuilder.append(parte).append("/");
                    }
                }

                if (parte.equals("upload")) {
                    comenzar = true;
                }
            }

            return publicIdBuilder.toString();
        } catch (Exception e) {
            throw new ErrorException409("No se pudo extraer el public_id desde la URL: " + url);
        }
    }


}
