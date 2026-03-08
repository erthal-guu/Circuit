package Circuit.Circuit.Service;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Model.Enum.StatusPedido;
import Circuit.Circuit.Repository.*;
import Circuit.Circuit.Service.Email.EmailService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

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

    @Autowired
    private ContasPagarService contasPagarService;

    public List<Pedido> listarPedidos() {
        List<StatusPedido> statusPedidos = Arrays.asList(
                StatusPedido.RECEBIDO,
                StatusPedido.PENDENTE,
                StatusPedido.CONFIRMADO,
                StatusPedido.CANCELADO);
        List<Pedido> pedidos = pedidoRepository.findByStatusIn(statusPedidos);
        logger.debug("Listagem de pedidos - {} registros encontrados", pedidos.size());
        return pedidos;
    }

    @Transactional
    public void salvarPedido(Long id, Long fornecedorId, Long responsavelId, String codigoPedido,
                             String observacao, String tipoPedido, LocalDate dataPedido, BigDecimal valorTotal,
                             List<Long> itensId, List<Integer> quantidadeItens, List<BigDecimal> precoItens,
                             Boolean notificarFornecedor) {

        if (itensId == null || itensId.isEmpty()) {
            logger.warn("Tentativa de salvar pedido sem itens");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido não pode ser salvo sem itens.");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> {
                    logger.error("Fornecedor não encontrado - ID: {}", fornecedorId);
                    return new RuntimeException("Fornecedor não encontrado");
                });

        Funcionario responsavel = funcionarioRepository.findById(responsavelId)
                .orElseThrow(() -> {
                    logger.error("Funcionário não encontrado - ID: {}", responsavelId);
                    return new RuntimeException("Funcionário não encontrado");
                });

        Pedido pedido;
        if (id != null) {
            logger.info("Tentativa de edição de pedido - ID: {}, Código: {}, Valor: {}",
                    id, codigoPedido, valorTotal);
            pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Pedido não encontrado - ID: {}", id);
                        return new RuntimeException("Pedido não encontrado");
                    });
            itemPedidoRepository.deleteAll(pedido.getItens());
            pedido.getItens().clear();
        } else {
            logger.info("Tentativa de cadastro de novo pedido - Código: {}, Valor: {}, Fornecedor: {}, Tipo: {}",
                    codigoPedido, valorTotal, fornecedor.getNomeFantasia(), tipoPedido);
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

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        for (int i = 0; i < itensId.size(); i++) {
            Long idAtual = itensId.get(i);

            ItemPedido item = new ItemPedido();
            item.setPedido(pedidoSalvo);
            item.setQuantidade(quantidadeItens.get(i));
            item.setPrecoUnitario(precoItens.get(i));
            item.setItemId(idAtual);
            item.setTipoItem(tipoPedido);

            if ("PRODUTO".equals(tipoPedido)) {
                Produto produto = produtoRepository.findById(idAtual)
                        .orElseThrow(() -> {
                            logger.error("Produto não encontrado - ID: {}", idAtual);
                            return new RuntimeException("Produto não encontrado");
                        });
                item.setProduto(produto);
            } else {
                Peca peca = pecaRepository.findById(idAtual)
                        .orElseThrow(() -> {
                            logger.error("Peça não encontrada - ID: {}", idAtual);
                            return new RuntimeException("Peça não encontrada");
                        });
                item.setPeca(peca);
            }
            itemPedidoRepository.save(item);
            pedidoSalvo.getItens().add(item);
        }

        if (Boolean.TRUE.equals(notificarFornecedor)) {
            logger.info("Enviando email de notificação para fornecedor - Pedido: {}", pedidoSalvo.getCodigo());
            emailService.enviarEmailPedidoPersonalizado(pedidoSalvo);
        }

        if (id == null) {
            logger.info("Pedido cadastrado com sucesso - ID: {}, Código: {}, Valor: {}",
                    pedidoSalvo.getId(), pedidoSalvo.getCodigo(), pedidoSalvo.getValorTotal());
        } else {
            logger.info("Pedido editado com sucesso - ID: {}, Código: {}, Valor: {}",
                    pedidoSalvo.getId(), pedidoSalvo.getCodigo(), pedidoSalvo.getValorTotal());
        }
    }

    public void atualizarStatus(Long id, StatusPedido novoStatus, Boolean deveNotificar) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> {
            logger.error("Pedido não encontrado para atualização de status - ID: {}", id);
            return new RuntimeException("Pedido não encontrado");
        });
        logger.info("Tentativa de atualizar status de pedido - ID: {}, Código: {}, Status atual: {}, Novo status: {}",
                id, pedido.getCodigo(), pedido.getStatus(), novoStatus);
        pedido.setStatus(novoStatus);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        if (novoStatus == StatusPedido.RECEBIDO) {
            logger.info("Pedido recebido, criando notificação e conta a pagar - ID: {}", id);
            notificacaoService.criarNotificacaoUnica(pedidoSalvo);
            contasPagarService.gerarContaPagarParaPedido(pedidoSalvo);
        }

        if (Boolean.TRUE.equals(deveNotificar)) {
            logger.info("Enviando email de notificação para fornecedor - Pedido: {}", pedidoSalvo.getCodigo());
            emailService.enviarEmailPedidoPersonalizado(pedidoSalvo);
        }

        logger.info("Status de pedido atualizado com sucesso - ID: {}, Novo status: {}", pedidoSalvo.getId(), pedidoSalvo.getStatus());
    }
}