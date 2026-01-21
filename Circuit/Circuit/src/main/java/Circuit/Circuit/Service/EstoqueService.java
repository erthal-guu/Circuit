package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Categoria;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.CategoriaRepository;
import Circuit.Circuit.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
    @Autowired
    private ProdutoRepository produtoRepository;

    public void alimentarEstoque(Long id, Integer quantidadeAdicionada) {
        Produto produto = produtoRepository.getReferenceById(id);
        produto.setQuantidade(produto.getQuantidade() + quantidadeAdicionada);
        produtoRepository.save(produto);
    }

    public void retirarEstoque(Long id, Integer quantidadeRetirada) {
        Produto produto = produtoRepository.getReferenceById(id);

        if (produto.getQuantidade() < quantidadeRetirada) {
            throw new RuntimeException("Estoque insuficiente!");
        }
        produto.setQuantidade(produto.getQuantidade() - quantidadeRetirada);
        produtoRepository.save(produto);
    }
}
