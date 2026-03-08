package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Peca;
import Circuit.Circuit.Model.Produto;
import Circuit.Circuit.Repository.PecaRepository;
import Circuit.Circuit.Repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PecaRepository pecasRepository;

    public void alimentarEstoqueProd(Long id, Integer quantidadeAdicionada) {
        Produto produto = produtoRepository.getReferenceById(id);
        logger.info("Tentativa de alimentar estoque de produto - ID: {}, Nome: {}, Quantidade atual: {}, Quantidade a adicionar: {}",
                produto.getId(), produto.getNome(), produto.getQuantidade(), quantidadeAdicionada);
        produto.setQuantidade(produto.getQuantidade() + quantidadeAdicionada);
        Produto produtoSalvo = produtoRepository.save(produto);
        logger.info("Estoque de produto alimentado com sucesso - ID: {}, Nome: {}, Nova quantidade: {}",
                produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getQuantidade());
    }

    @Transactional
    public void retirarEstoqueProd(Long id, Integer quantidadeRetirada) {
        Produto produto = produtoRepository.getReferenceById(id);

        if (produto.getQuantidade() < quantidadeRetirada) {
            logger.warn("Tentativa de retirar estoque insuficiente de produto - ID: {}, Nome: {}, Quantidade atual: {}, Quantidade a retirar: {}",
                    produto.getId(), produto.getNome(), produto.getQuantidade(), quantidadeRetirada);
            throw new RuntimeException("Estoque insuficiente!");
        }
        logger.info("Tentativa de retirar estoque de produto - ID: {}, Nome: {}, Quantidade atual: {}, Quantidade a retirar: {}",
                produto.getId(), produto.getNome(), produto.getQuantidade(), quantidadeRetirada);
        produto.setQuantidade(produto.getQuantidade() - quantidadeRetirada);
        Produto produtoSalvo = produtoRepository.save(produto);
        logger.info("Estoque de produto retirado com sucesso - ID: {}, Nome: {}, Nova quantidade: {}",
                produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getQuantidade());
    }

    public void alimentarEstoquePeca(Long id, Integer quantidadeAdicionada) {
        Peca pecas = pecasRepository.getReferenceById(id);
        logger.info("Tentativa de alimentar estoque de peça - ID: {}, Nome: {}, Quantidade atual: {}, Quantidade a adicionar: {}",
                pecas.getId(), pecas.getNome(), pecas.getQuantidade(), quantidadeAdicionada);
        pecas.setQuantidade(pecas.getQuantidade() + quantidadeAdicionada);
        Peca pecaSalva = pecasRepository.save(pecas);
        logger.info("Estoque de peça alimentado com sucesso - ID: {}, Nome: {}, Nova quantidade: {}",
                pecaSalva.getId(), pecaSalva.getNome(), pecaSalva.getQuantidade());
    }

    public void retirarEstoquePeca(Long id, Integer quantidadeRetirada) {
        Peca pecas = pecasRepository.getReferenceById(id);
        if (pecas.getQuantidade() < quantidadeRetirada) {
            logger.warn("Tentativa de retirar estoque insuficiente de peça - ID: {}, Nome: {}, Quantidade atual: {}, Quantidade a retirar: {}",
                    pecas.getId(), pecas.getNome(), pecas.getQuantidade(), quantidadeRetirada);
            throw new RuntimeException("Estoque insuficiente!");
        }
        logger.info("Tentativa de retirar estoque de peça - ID: {}, Nome: {}, Quantidade atual: {}, Quantidade a retirar: {}",
                pecas.getId(), pecas.getNome(), pecas.getQuantidade(), quantidadeRetirada);
        pecas.setQuantidade(pecas.getQuantidade() - quantidadeRetirada);
        Peca pecaSalva = pecasRepository.save(pecas);
        logger.info("Estoque de peça retirado com sucesso - ID: {}, Nome: {}, Nova quantidade: {}",
                pecaSalva.getId(), pecaSalva.getNome(), pecaSalva.getQuantidade());
    }
}
