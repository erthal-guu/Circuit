package Circuit.Circuit.Service;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private PecaRepository pecaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public void salvarPedido(Long fornecedorId, Long responsavelId, String codigoPedido,
                             String observacao, String tipoPedido, LocalDate dataPedido, BigDecimal valorTotal,
                             List<Long> itensId, List<Integer> quantidadeItens, List<BigDecimal> precoItens) {

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Funcionario responsavel = funcionarioRepository.findById(responsavelId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCodigo(codigoPedido);
        pedido.setDataPedido(dataPedido);
        pedido.setObservacoes(observacao);
        pedido.setTipoPedido(tipoPedido);
        pedido.setFornecedor(fornecedor);
        pedido.setResponsavel(responsavel);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setValorTotal(valorTotal);

        pedidoRepository.save(pedido);

        if (itensId != null) {
            for (int i = 0; i < itensId.size(); i++) {
                Long idAtual = itensId.get(i);

                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setQuantidade(quantidadeItens.get(i));
                item.setPrecoUnitario(precoItens.get(i));
                item.setItemId(idAtual);
                item.setTipoItem(tipoPedido);

                if ("PRODUTO".equals(tipoPedido)) {
                    Produto produto = produtoRepository.findById(idAtual)
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                    item.setProduto(produto);
                } else {
                    Peca peca = pecaRepository.findById(idAtual)
                            .orElseThrow(() -> new RuntimeException("Peça não encontrada"));
                    item.setPeca(peca);
                }

                itemPedidoRepository.save(item);
            }
        }
    }
    public void atualizarStatus(Long id, StatusPedido novoStatus){
        Pedido pedido = pedidoRepository.findById(id).get();
        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);
    }
}