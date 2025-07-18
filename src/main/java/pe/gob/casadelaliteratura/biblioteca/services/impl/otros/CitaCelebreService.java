package pe.gob.casadelaliteratura.biblioteca.services.impl.otros;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.gob.casadelaliteratura.biblioteca.dtos.otros.CitaCelebreDto;
import pe.gob.casadelaliteratura.biblioteca.models.otros.CitaCelebre;
import pe.gob.casadelaliteratura.biblioteca.utils.exceptions.ResourceNotFoundException;
import java.util.List;

@Service
public class CitaCelebreService {

    private final RestTemplate restTemplate = new RestTemplate();

    public CitaCelebreDto getCitaAleatoria() {

        String urlApiCitas = "https://thequoteshub.com/api/";
        CitaCelebre contenidoCita;

        do {
            contenidoCita = restTemplate.getForObject(urlApiCitas, CitaCelebre.class);

            if (contenidoCita == null) {
                throw new ResourceNotFoundException("Error al obtener la cita reflexiva.");
            }

        } while (contenidoCita.getText().length() >= 55);

        return CitaCelebreDto.builder()
                .autor(contenidoCita.getAuthor())
                .cita(traducirTexto(contenidoCita.getText()))
                .build();
    }

    public String traducirTexto(String texto) {
        try {
            texto = texto
                    .replaceAll("’", "'")
                    .replaceAll("‘", "'")
                    .replaceAll("-as", " as")
                    .replaceAll("'s", " is")
                    .replaceAll("n't", " not")
                    .replaceAll("'re", " are")
                    .replaceAll("'ll", " will")
                    .replaceAll("'d", " would")
                    .replaceAll("'m", " am")
                    .replaceAll("\\.'", ".")
                    .replaceAll("'", "");

            String urlTraduccion = "https://translate.googleapis.com/translate_a/single" +
                    "?client=gtx&sl=auto&tl=es&dt=t&q=" + texto;

            String json = restTemplate.getForObject(urlTraduccion, String.class);

            ObjectMapper mapper = new ObjectMapper();
            List<?> root = mapper.readValue(json, List.class);

            if (root != null && !root.isEmpty()) {
                List<?> traducciones = (List<?>) root.getFirst();

                StringBuilder resultado = new StringBuilder();
                for (Object obj : traducciones) {
                    if (obj instanceof List<?> fragmento) {
                        if (!fragmento.isEmpty()) {
                            String frase = (String) fragmento.getFirst();
                            resultado.append(frase);
                        }
                    }
                }
                return resultado.toString().trim();
            }

        } catch (Exception e) {
            System.err.println("Error al traducir: " + e.getMessage());
        }

        return texto;
    }

}
