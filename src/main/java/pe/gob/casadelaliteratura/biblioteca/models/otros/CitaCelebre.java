package pe.gob.casadelaliteratura.biblioteca.models.otros;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CitaCelebre {

    private String text;
    private String author;
    private List<String> tags;
    private Long id;
    private String author_id;

}
