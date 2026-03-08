package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Model.Pedido;
import Circuit.Circuit.Repository.ContaPagarRepository;
import Circuit.Circuit.Repository.FornecedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContasPagarService {

    private static final Logger logger = LoggerFactory.getLogger(ContasPagarService.class);

    @Autowired
    ContaPagarRepository contaPagarRepository;

    @Autowired
    FornecedorRepository fornecedorRepository;

    public void gerarContaPagarParaPedido(Pedido pedido) {
        boolean pedidoJaGerado = contaPagarRepository.existsByOrigemAndOrigemId("Pedido", pedido.getId());
        if (pedidoJaGerado) {
            logger.debug("Conta a pagar já existe para pedido #{}", pedido.getCodigo());
            return;
        }
        logger.info("Gerando conta a pagar para pedido #{} - Valor: {}, Fornecedor: {}",
                pedido.getCodigo(), pedido.getValorTotal(), pedido.getFornecedor() != null ? pedido.getFornecedor().getNomeFantasia() : "N/A");
        ContasPagar contasPagar = new ContasPagar();
        contasPagar.setOrigem("Pedido");
        contasPagar.setOrigemId(pedido.getId());
        contasPagar.setValor(pedido.getValorTotal());
        contasPagar.setDataVencimento(pedido.getDataPedido().plusDays(30));
        contasPagar.setStatus(StatusFinanceiro.PENDENTE);
        contasPagar.setFornecedor(pedido.getFornecedor());
        contasPagar.setDescricao("Pedido #" + pedido.getCodigo());
        ContasPagar contaSalva = contaPagarRepository.save(contasPagar);
        logger.info("Conta a pagar gerada com sucesso - ID: {}, Valor: {}, Vencimento: {}",
                contaSalva.getId(), contaSalva.getValor(), contaSalva.getDataVencimento());
    }

    public List<ContasPagar> listarContasPagar(){
        List<ContasPagar> contas = contaPagarRepository.findAll();
        logger.debug("Listagem de contas a pagar - {} registros encontrados", contas.size());
        return contas;
    }

    public ContasPagar salvarContaPagar(ContasPagar contaPagar) {
        logger.info("Tentativa de salvar conta a pagar - Valor: {}, Vencimento: {}, Fornecedor: {}",
                contaPagar.getValor(), contaPagar.getDataVencimento(),
                contaPagar.getFornecedor() != null ? contaPagar.getFornecedor().getNomeFantasia() : "N/A");
        return contaPagarRepository.save(contaPagar);
    }

    public ContasPagar buscarPorId(Long id) {
        return contaPagarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta a pagar não encontrada - ID: {}", id);
                    return new RuntimeException("Conta a pagar não encontrada");
                });
    }

    public void deletarContaPagar(Long id) {
        ContasPagar conta = buscarPorId(id);
        logger.info("Tentativa de exclusão de conta a pagar - ID: {}, Valor: {}, Descrição: {}",
                conta.getId(), conta.getValor(), conta.getDescricao());
        contaPagarRepository.delete(conta);
        logger.info("Conta a pagar excluída com sucesso - ID: {}", id);
    }

    public void pagarConta(Long id, BigDecimal valorPago, LocalDate dataPagamento, FormaPagamento formaPagamento) {
        ContasPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para pagamento - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });
        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            logger.warn("Tentativa de pagar conta já paga - ID: {}", id);
            throw new RuntimeException("Não é possível pagar uma conta com status PAGO");
        }
        if (conta.getStatus() == StatusFinanceiro.CANCELADA) {
            logger.warn("Tentativa de pagar conta cancelada - ID: {}", id);
            throw new RuntimeException("Não é possível pagar uma conta cancelada");
        }

        BigDecimal valorTotal = conta.getValor();
        if (valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Tentativa de pagar conta com valor inválido - ID: {}, Valor: {}", id, valorPago);
            throw new RuntimeException("Valor pago deve ser maior que zero");
        }
        if (valorPago.compareTo(valorTotal) > 0) {
            logger.warn("Tentativa de pagar conta com valor acima do total - ID: {}, Valor pago: {}, Valor total: {}",
                    id, valorPago, valorTotal);
            throw new RuntimeException("Valor pago não pode ser maior que o valor total da conta");
        }

        logger.info("Tentativa de pagamento de conta - ID: {}, Valor pago: {}, Data: {}, Forma: {}",
                id, valorPago, dataPagamento, formaPagamento);
        conta.setValorPago(valorPago);
        conta.setDataPagamento(dataPagamento);
        if (formaPagamento != null) {
            conta.setFormaPagamento(formaPagamento);
        }
        if (valorPago.compareTo(valorTotal) < 0) {
            conta.setStatus(StatusFinanceiro.PARCIAL);
        } else if (valorPago.compareTo(valorTotal) == 0) {
            conta.setStatus(StatusFinanceiro.PAGO);
        }

        ContasPagar contaSalva = contaPagarRepository.save(conta);
        logger.info("Conta paga com sucesso - ID: {}, Valor pago: {}, Status: {}",
                contaSalva.getId(), contaSalva.getValorPago(), contaSalva.getStatus());
    }

    public BigDecimal calcularTotalPendente() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.PENDENTE);
        BigDecimal total = contas.stream().map(ContasPagar::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Total de contas pendentes: {}", total);
        return total;
    }

    public BigDecimal calcularTotalPago() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.PAGO);
        BigDecimal total = contas.stream().map(ContasPagar::getValorPago).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Total de contas pagas: {}", total);
        return total;
    }

    public BigDecimal calcularTotalCancelado() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.CANCELADA);
        BigDecimal total = contas.stream().map(ContasPagar::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Total de contas canceladas: {}", total);
        return total;
    }

    public void cancelarConta(Long id) {
        ContasPagar contasPagar = contaPagarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para cancelamento - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });

        if (contasPagar.getStatus() == StatusFinanceiro.CANCELADA) {
            logger.warn("Tentativa de cancelar conta já cancelada - ID: {}", id);
            throw new RuntimeException("Não é possível cancelar uma conta já cancelada");
        }

        if (contasPagar.getStatus() == StatusFinanceiro.PAGO) {
            logger.warn("Tentativa de cancelar conta já paga - ID: {}", id);
            throw new RuntimeException("Não é possível cancelar uma conta já paga");
        }

        logger.info("Tentativa de cancelamento de conta - ID: {}, Valor: {}, Descrição: {}",
                contasPagar.getId(), contasPagar.getValor(), contasPagar.getDescricao());
        contasPagar.setStatus(StatusFinanceiro.CANCELADA);
        contasPagar.setValorPago(BigDecimal.ZERO);
        contaPagarRepository.save(contasPagar);
        logger.info("Conta cancelada com sucesso - ID: {}", id);
    }

    public void editarConta(Long id, Long fornecedorId, BigDecimal valor, BigDecimal valorPago,
                          LocalDate dataVencimento, LocalDate dataPagamento, FormaPagamento formaPagamento,
                          CondicaoPagamento condicaoPagamento, Integer numeroParcelas) {
        ContasPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para edição - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });

        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            logger.warn("Tentativa de editar conta já paga - ID: {}", id);
            throw new RuntimeException("Não é possível editar uma conta com status PAGO");
        }
        if (conta.getStatus() == StatusFinanceiro.PARCIAL) {
            logger.warn("Tentativa de editar conta com pagamento parcial - ID: {}", id);
            throw new RuntimeException("Não é possível editar uma conta com status PARCIAL");
        }
        if (conta.getStatus() == StatusFinanceiro.CANCELADA) {
            logger.warn("Tentativa de editar conta cancelada - ID: {}", id);
            throw new RuntimeException("Não é possível editar uma conta cancelada");
        }

        logger.info("Tentativa de edição de conta - ID: {}, Vencimento novo: {}, Valor novo: {}",
                id, dataVencimento, valor);
        conta.setDataVencimento(dataVencimento);
        if (dataPagamento != null) {
            conta.setDataPagamento(dataPagamento);
        }
        if (formaPagamento != null) {
            conta.setFormaPagamento(formaPagamento);
        }
        if (condicaoPagamento != null) {
            conta.setCondicaoPagamento(condicaoPagamento);
        }
        if (numeroParcelas != null) {
            conta.setNumeroParcelas(numeroParcelas);
        }

        if (fornecedorId != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId).orElse(null);
            if (fornecedor != null) {
                conta.setFornecedor(fornecedor);
            }
        }

        ContasPagar contaSalva = contaPagarRepository.save(conta);
        logger.info("Conta editada com sucesso - ID: {}", contaSalva.getId());
    }
}
