package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PecaService {

    @Autowired
    private PecaRepository pecasRepository;

    public Peca cadastrar(Peca pecas){
        return pecasRepository.save(pecas);
    }

    public Peca excluirPeca(Long id){
        Peca peca = pecasRepository.getReferenceById(id);
        peca.setAtivo(false);
        return pecasRepository.save(peca);
    }
    public Peca restaurarPeca(Long id){
        Peca pecas = pecasRepository.getReferenceById(id);
        pecas.setAtivo(true);
        return  pecasRepository.save(pecas);
    }

    public List<Peca> listarPecasAtivas(){
        return pecasRepository.findByAtivoTrueOrderById();
    }
    public List<Peca> listarPecasInativas(){
        return pecasRepository.findByAtivoFalseOrderById();
    }
    public Peca editarPeca(Long id, Peca dadosAtualizados) {
        Peca pecas = pecasRepository.getReferenceById(id);

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
    public BigDecimal valorTotalPecas(){
        return pecasRepository.sumPrecoVenda();
    }
}
