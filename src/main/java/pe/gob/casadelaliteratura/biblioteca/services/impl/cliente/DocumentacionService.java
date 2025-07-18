package pe.gob.casadelaliteratura.biblioteca.services.impl.cliente;

import org.springframework.stereotype.Service;
import pe.gob.casadelaliteratura.biblioteca.models.cliente.Documentacion;
import pe.gob.casadelaliteratura.biblioteca.repositories.cliente.DocumentacionRepository;
import pe.gob.casadelaliteratura.biblioteca.services.interfaces.cliente.IDocumentacionService;

@Service
public class DocumentacionService implements IDocumentacionService {

    private final DocumentacionRepository repository;

    public DocumentacionService(DocumentacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Documentacion saveOrUpdate(Documentacion entity) {
        return repository.save(entity);
    }
}
