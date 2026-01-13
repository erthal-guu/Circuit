package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Categoria;
import Circuit.Circuit.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
    @Autowired
    private CategoriaRepository estoqueRepository;

    public List<Categoria> ListarCategorias(){
        return estoqueRepository.findAllByOrderByTipoAsc();
    }
}
