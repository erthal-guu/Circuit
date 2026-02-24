package Circuit.Circuit.Service;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Repository.*;
import Circuit.Circuit.Service.Email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificacaoService notificacaoService;

    public List<Pedido> listarPedidos() {
        List<StatusPedido> statusPedidos = Arrays.asList(
                StatusPedido.RECEBIDO,
                StatusPedido.PENDENTE,
                StatusPedido.CONFIRMADO);
        return pedidoRepository.findByStatusIn(statusPedidos);
    }

    @Transactional
    public void salvarPedido(Long id, Long fornecedorId, Long responsavelId, String codigoPedido,
                             String observacao, String tipoPedido, LocalDate dataPedido, BigDecimal valorTotal,
                             List<Long> itensId, List<Integer> quantidadeItens, List<BigDecimal> precoItens,
                             Boolean notificarFornecedor) {

        if (itensId == null || itensId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido não pode ser salvo sem itens.");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Funcionario responsavel = funcionarioRepository.findById(responsavelId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Pedido pedido;
        if (id != null) {
            pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
            itemPedidoRepository.deleteAll(pedido.getItens());
            pedido.getItens().clear();
        } else {
            pedido = new Pedido();
            pedido.setCodigo(codigoPedido);
            pedido.setStatus(StatusPedido.PENDENTE);
            pedido.setItens(new ArrayList<>());
        }

        pedido.setDataPedido(dataPedido);
        pedido.setObservacoes(observacao);
        pedido.setTipoPedido(tipoPedido);
        pedido.setFornecedor(fornecedor);
        pedido.setResponsavel(responsavel);
        pedido.setValorTotal(valorTotal);

        pedidoRepository.save(pedido);

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
            pedido.getItens().add(item);
        }

        if (Boolean.TRUE.equals(notificarFornecedor)) {
            emailService.enviarEmailPedidoPersonalizado(pedido);
        }
    }

    public void atualizarStatus(Long id, StatusPedido novoStatus, Boolean deveNotificar) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow();
        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);

        if (novoStatus == StatusPedido.RECEBIDO) {
            notificacaoService.criarNotificacaoUnica(pedido);
        }

        if (Boolean.TRUE.equals(deveNotificar)) {
            emailService.enviarEmailPedidoPersonalizado(pedido);
        }
    }
}