package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Pecas;
import Circuit.Circuit.Repository.PecasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecasService {

    @Autowired
    private PecasRepository pecasRepository;

    public Pecas cadastrar(Pecas pecas){
        pecas.setAtivo(true);
        return pecasRepository.save(pecas);
    }

    public Pecas excluirPeca(Long id){
        Pecas pecas = pecasRepository.getReferenceById(id);
        pecas.setAtivo(false);
        return pecasRepository.save(pecas);
    }
    public Pecas restaurarPeca(Long id){
        Pecas pecas = pecasRepository.getReferenceById(id);
        pecas.setAtivo(true);
        return  pecasRepository.save(pecas);
    }

    public List<Pecas> listarPecasAtivas(){
        return pecasRepository.findByAtivoTrueOrderById();
    }
    public List<Pecas> listarPecasInativas(){
        return pecasRepository.findByAtivoFalseOrderById();
    }
    public Pecas editarPeca(Long id, Pecas dadosAtualizados) {
        Pecas pecas = pecasRepository.getReferenceById(id);

        pecas.setNome(dadosAtualizados.getNome());
        pecas.setQuantidade(dadosAtualizados.getQuantidade());
        pecas.setQuantidadeMinima(dadosAtualizados.getQuantidadeMinima());
        pecas.setPrecoCompra(dadosAtualizados.getPrecoCompra());
        pecas.setPrecoVenda(dadosAtualizados.getPrecoVenda());
        pecas.setCategoria(dadosAtualizados.getCategoria());
        pecas.setFornecedor(dadosAtualizados.getFornecedor());
        pecas.setCodigoBarras(dadosAtualizados.getCodigoBarras());
        pecas.setAtivo(dadosAtualizados.getAtivo());

        return pecasRepository.save(pecas);
    }
    public long contarCriticos() {
        return pecasRepository.countItensCriticos();
    }
}
