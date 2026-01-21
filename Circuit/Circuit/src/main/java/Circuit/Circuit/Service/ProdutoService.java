package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto cadastrar(Produto produto) {
        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findByAtivoTrueOrderById();
    }

    public List<Produto> listarProdutosInativos() {
        return produtoRepository.findByAtivoFalseOrderById();
    }

    public void excluirProduto(Long id) {
        Produto produto = produtoRepository.getReferenceById(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    public void restaurarProduto(Long id) {
        Produto produto = produtoRepository.getReferenceById(id);
        produto.setAtivo(true);
        produtoRepository.save(produto);
    }

    public Produto editarProduto(Long id, Produto dadosAtualizados) {
        Produto produtoEditar = produtoRepository.getReferenceById(id);

        produtoEditar.setNome(dadosAtualizados.getNome());
        produtoEditar.setQuantidade(dadosAtualizados.getQuantidade());
        produtoEditar.setQuantidadeMinima(dadosAtualizados.getQuantidadeMinima());
        produtoEditar.setPrecoCompra(dadosAtualizados.getPrecoCompra());
        produtoEditar.setPrecoVenda(dadosAtualizados.getPrecoVenda());
        produtoEditar.setCategoria(dadosAtualizados.getCategoria());
        produtoEditar.setFornecedor(dadosAtualizados.getFornecedor());
        produtoEditar.setAtivo(dadosAtualizados.getAtivo());
        return produtoRepository.save(produtoEditar);
    }


    public long contarCriticos() {
        return produtoRepository.countItensCriticos();
    }

}