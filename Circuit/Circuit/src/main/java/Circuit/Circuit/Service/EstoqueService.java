package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.PecaRepository;
import Circuit.Circuit.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private PecaRepository pecasRepository;

    public void alimentarEstoqueProd(Long id, Integer quantidadeAdicionada) {
        Produto produto = produtoRepository.getReferenceById(id);
        produto.setQuantidade(produto.getQuantidade() + quantidadeAdicionada);
        produtoRepository.save(produto);
    }

    public void retirarEstoqueProd(Long id, Integer quantidadeRetirada) {
        Produto produto = produtoRepository.getReferenceById(id);

        if (produto.getQuantidade() < quantidadeRetirada) {
            throw new RuntimeException("Estoque insuficiente!");
        }
        produto.setQuantidade(produto.getQuantidade() - quantidadeRetirada);
        produtoRepository.save(produto);
    }
    public void alimentarEstoquePeca(Long id, Integer quantidadeAdicionada) {
        Peca pecas = pecasRepository.getReferenceById(id);
        pecas.setQuantidade(pecas.getQuantidade() + quantidadeAdicionada);
        pecasRepository.save(pecas);
    }

    public void retirarEstoquePeca(Long id, Integer quantidadeRetirada) {
        Peca pecas = pecasRepository.getReferenceById(id);
        if (pecas.getQuantidade() < quantidadeRetirada) {
            throw new RuntimeException("Estoque insuficiente!");
        }
        pecas.setQuantidade(pecas.getQuantidade() - quantidadeRetirada);
        pecasRepository.save(pecas);
    }
}
