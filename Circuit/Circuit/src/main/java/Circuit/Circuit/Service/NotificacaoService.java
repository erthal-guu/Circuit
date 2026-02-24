package Circuit.Circuit.Service;

import Circuit.Circuit.Model.*;
import Circuit.Circuit.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;


    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Notificacao> listarPendentes(String tipo) {
        return notificacaoRepository.findPendentesByTipo(tipo.toUpperCase());
    }


    @Transactional
    public void efetivarEntrada(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

        Pedido pedido = notificacao.getPedido();

        for (ItemPedido item : pedido.getItens()) {
            if ("PECA".equals(pedido.getTipoPedido()) && item.getPeca() != null) {
                Peca peca = pecaRepository.findById(item.getPeca().getId())
                        .orElseThrow(() -> new RuntimeException("Peça não encontrada"));
                peca.setQuantidade(peca.getQuantidade() + item.getQuantidade());
                pecaRepository.save(peca);

            } else if ("PRODUTO".equals(pedido.getTipoPedido()) && item.getProduto() != null) {
                Produto produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
                produtoRepository.save(produto);
            }
        }

        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }
    public void criarNotificacaoUnica(Pedido pedido) {
        boolean jaExiste = notificacaoRepository.existsByPedidoAndLidaFalse(pedido);
        if (!jaExiste) {
            Notificacao notificacao = new Notificacao();
            notificacao.setPedido(pedido);
            notificacao.setTipo(pedido.getTipoPedido().toUpperCase());
            notificacao.setLida(false);
            notificacao.setMensagem("O pedido " + pedido.getCodigo() + " foi recebido. Efetive para atualizar o saldo.");
            notificacaoRepository.save(notificacao);
        }
    }

}