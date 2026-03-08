package Circuit.Circuit.Service;

import Circuit.Circuit.Model.Cliente;
import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.Venda;
import Circuit.Circuit.Repository.ClienteRepository;
import Circuit.Circuit.Repository.ContaReceberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContaReceberService {

    private static final Logger logger = LoggerFactory.getLogger(ContaReceberService.class);

    @Autowired
    ContaReceberRepository contaReceberRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public void gerarContaReceberParaVenda(Venda venda) {
        boolean VendaJaGerada = contaReceberRepository.existsByOrigemAndOrigemId("Venda", venda.getId());
        if (VendaJaGerada){
            logger.debug("Conta a receber já existe para venda #{}", venda.getId());
            return;
        }
        logger.info("Gerando conta a receber para venda #{} - Valor: {}, Cliente: {}",
                venda.getId(), venda.getValorTotal(), venda.getCliente() != null ? venda.getCliente().getNome() : "N/A");
        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setOrigem("Venda");
        contasReceber.setOrigemId(venda.getId());
        contasReceber.setValor(venda.getValorTotal());
        contasReceber.setDataVencimento(venda.getDataVenda().plusDays(30));
        contasReceber.setStatus(StatusFinanceiro.PENDENTE);
        contasReceber.setCliente(venda.getCliente());
        ContasReceber contaSalva = contaReceberRepository.save(contasReceber);
        logger.info("Conta a receber gerada com sucesso - ID: {}, Valor: {}, Vencimento: {}",
                contaSalva.getId(), contaSalva.getValor(), contaSalva.getDataVencimento());
    }

    public void gerarContaReceberParaOS(OrdemServico ordemServico) {
        boolean OSjaGerada = contaReceberRepository.existsByOrigemAndOrigemId("Ordem de serviço", ordemServico.getId());
        if (OSjaGerada) {
            logger.debug("Conta a receber já existe para OS #{}", ordemServico.getId());
            return;
        }
        logger.info("Gerando conta a receber para OS #{} - Valor: {}, Cliente: {}",
                ordemServico.getId(), ordemServico.getValorTotal(),
                ordemServico.getCliente() != null ? ordemServico.getCliente().getNome() : "N/A");
        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setOrigem("Ordem de serviço");
        contasReceber.setOrigemId(ordemServico.getId());
        contasReceber.setValor(ordemServico.getValorTotal());
        contasReceber.setDataVencimento(ordemServico.getDataEntrada().plusDays(30).toLocalDate());
        contasReceber.setStatus(StatusFinanceiro.PENDENTE);
        contasReceber.setCliente(ordemServico.getCliente());
        ContasReceber contaSalva = contaReceberRepository.save(contasReceber);
        logger.info("Conta a receber gerada com sucesso - ID: {}, Valor: {}, Vencimento: {}",
                contaSalva.getId(), contaSalva.getValor(), contaSalva.getDataVencimento());
    }

    public List<ContasReceber> listarContasReceber(){
        List<ContasReceber> contas = contaReceberRepository.findAll();
        logger.debug("Listagem de contas a receber - {} registros encontrados", contas.size());
        return contas;
    }

    public void receberPagamento(Long id, BigDecimal valorRecebido, LocalDate dataPagamento, FormaPagamento formaPagamento, CondicaoPagamento condicaoPagamento, Integer numeroParcelas) {
        ContasReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para recebimento - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });

        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            logger.warn("Tentativa de receber pagamento de conta já paga - ID: {}", id);
            throw new RuntimeException("Não é possível receber pagamento de uma conta com status PAGO");
        }

        if (conta.getStatus() == StatusFinanceiro.CANCELADA) {
            logger.warn("Tentativa de receber pagamento de conta cancelada - ID: {}", id);
            throw new RuntimeException("Não é possível receber pagamento de uma conta cancelada");
        }

        BigDecimal valorTotal = conta.getValor();
        if (valorRecebido.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Tentativa de receber pagamento com valor inválido - ID: {}, Valor: {}", id, valorRecebido);
            throw new RuntimeException("Valor recebido deve ser maior que zero");
        }
        if (valorRecebido.compareTo(valorTotal) > 0) {
            logger.warn("Tentativa de receber pagamento com valor acima do total - ID: {}, Valor recebido: {}, Valor total: {}",
                    id, valorRecebido, valorTotal);
            throw new RuntimeException("Valor recebido não pode ser maior que o valor total da conta");
        }

        logger.info("Tentativa de recebimento de pagamento - ID: {}, Valor recebido: {}, Data: {}, Forma: {}, Condição: {}, Parcelas: {}",
                id, valorRecebido, dataPagamento, formaPagamento, condicaoPagamento, numeroParcelas);
        conta.setValorRecebido(valorRecebido);
        conta.setDataPagamento(dataPagamento);
        if (formaPagamento != null) {
            conta.setFormaPagamento(formaPagamento);
        }
        if (condicaoPagamento != null) {
            conta.setCondicaoPagamento(condicaoPagamento);
        }
        if (numeroParcelas != null) {
            conta.setNumeroParcelas(numeroParcelas);
        }
        if (valorRecebido.compareTo(valorTotal) < 0) {
            conta.setStatus(StatusFinanceiro.PARCIAL);
        } else if (valorRecebido.compareTo(valorTotal) == 0) {
            conta.setStatus(StatusFinanceiro.PAGO);
        }

        ContasReceber contaSalva = contaReceberRepository.save(conta);
        logger.info("Pagamento recebido com sucesso - ID: {}, Valor recebido: {}, Status: {}",
                contaSalva.getId(), contaSalva.getValorRecebido(), contaSalva.getStatus());
    }

    public BigDecimal calcularTotalPendente() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.PENDENTE);
        BigDecimal total = contas.stream().map(ContasReceber::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Total de contas pendentes: {}", total);
        return total;
    }

    public BigDecimal calcularTotalRecebido() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.PAGO);
        BigDecimal total = contas.stream().map(ContasReceber::getValorRecebido).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Total de contas recebidas: {}", total);
        return total;
    }

    public BigDecimal calcularTotalCancelado() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.CANCELADA);
        BigDecimal total = contas.stream().map(ContasReceber::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Total de contas canceladas: {}", total);
        return total;
    }

    public void atualizarStatus(Long id, StatusFinanceiro novoStatus) {
        ContasReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para atualização de status - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });
        if (conta.getStatus() == StatusFinanceiro.PAGO && novoStatus == StatusFinanceiro.PENDENTE) {
            logger.warn("Tentativa de alterar status de PAGO para PENDENTE - ID: {}", id);
            throw new RuntimeException("Não é possível alterar o status de uma conta PAGO para PENDENTE");
        }

        logger.info("Tentativa de atualização de status - ID: {}, Status atual: {}, Novo status: {}",
                id, conta.getStatus(), novoStatus);
        conta.setStatus(novoStatus);
        ContasReceber contaSalva = contaReceberRepository.save(conta);
        logger.info("Status atualizado com sucesso - ID: {}, Novo status: {}", contaSalva.getId(), contaSalva.getStatus());
    }

    public void cancelarConta(Long id){
        ContasReceber contasReceber = contaReceberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para cancelamento - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });

        if (contasReceber.getStatus() == StatusFinanceiro.CANCELADA) {
            logger.warn("Tentativa de cancelar conta já cancelada - ID: {}", id);
            throw new RuntimeException("Não é possível cancelar uma conta já cancelada");
        }

        if (contasReceber.getStatus() == StatusFinanceiro.PAGO) {
            logger.warn("Tentativa de cancelar conta já paga - ID: {}", id);
            throw new RuntimeException("Não é possível cancelar uma conta já paga");
        }

        logger.info("Tentativa de cancelamento de conta - ID: {}, Valor: {}, Cliente: {}",
                contasReceber.getId(), contasReceber.getValor(),
                contasReceber.getCliente() != null ? contasReceber.getCliente().getNome() : "N/A");
        contasReceber.setStatus(StatusFinanceiro.CANCELADA);
        contasReceber.setValorRecebido(BigDecimal.ZERO);
        contaReceberRepository.save(contasReceber);
        logger.info("Conta cancelada com sucesso - ID: {}", id);
    }

    public void editarConta(Long id, Long clienteId, BigDecimal valor, BigDecimal valorRecebido,
                          LocalDate dataVencimento, LocalDate dataPagamento, FormaPagamento formaPagamento,
                          CondicaoPagamento condicaoPagamento, Integer numeroParcelas) {
        ContasReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Conta não encontrada para edição - ID: {}", id);
                    return new RuntimeException("Conta não encontrada");
                });

        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            logger.warn("Tentativa de editar conta já paga - ID: {}", id);
            throw new RuntimeException("Não é possível editar uma conta com status PAGO");
        }
        if (conta.getStatus() == StatusFinanceiro.PARCIAL) {
            logger.warn("Tentativa de editar conta com recebimento parcial - ID: {}", id);
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

        if (clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
            if (cliente != null) {
                conta.setCliente(cliente);
            }
        }

        ContasReceber contaSalva = contaReceberRepository.save(conta);
        logger.info("Conta editada com sucesso - ID: {}", contaSalva.getId());
    }
}
