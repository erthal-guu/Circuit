package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.Venda;
import Circuit.Circuit.Repository.ContaReceberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContaReceberService {
    @Autowired
    ContaReceberRepository contaReceberRepository;

    public void gerarContaReceberParaVenda(Venda venda ) {
        boolean VendaJaGerada = contaReceberRepository.existsByOrigemAndOrigemId("Venda", venda.getId());
        if (VendaJaGerada){
            return;
        }
        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setOrigem("Venda");
        contasReceber.setOrigemId(venda.getId());
        contasReceber.setValor(venda.getValorTotal());
        contasReceber.setDataVencimento(venda.getDataVenda().plusDays(30));
        contasReceber.setStatus(StatusFinanceiro.PENDENTE);
        contasReceber.setCliente(venda.getCliente());
        contaReceberRepository.save(contasReceber);
    }

    public void gerarContaReceberParaOS(OrdemServico ordemServico) {
        boolean OSjaGerada = contaReceberRepository.existsByOrigemAndOrigemId("Ordem de serviço", ordemServico.getId());
        if (OSjaGerada) {
            return;
        }
        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setOrigem("Ordem de serviço");
        contasReceber.setOrigemId(ordemServico.getId());
        contasReceber.setValor(ordemServico.getValorTotal());
        contasReceber.setDataVencimento(ordemServico.getDataEntrada().plusDays(30).toLocalDate());
        contasReceber.setStatus(StatusFinanceiro.PENDENTE);
        contasReceber.setCliente(ordemServico.getCliente());
        contaReceberRepository.save(contasReceber);
    }
    public List<ContasReceber> listarContasReceber(){
        return contaReceberRepository.findAll();
    }
    
    public void receberPagamento(Long id, BigDecimal valorRecebido, LocalDate dataPagamento, FormaPagamento formaPagamento, CondicaoPagamento condicaoPagamento, Integer numeroParcelas) {
        ContasReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            throw new RuntimeException("Não é possível receber pagamento de uma conta com status PAGO");
        }
        
        BigDecimal valorTotal = conta.getValor();
        if (valorRecebido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor recebido deve ser maior que zero");
        }
        if (valorRecebido.compareTo(valorTotal) > 0) {
            throw new RuntimeException("Valor recebido não pode ser maior que o valor total da conta");
        }
        
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
        
        contaReceberRepository.save(conta);
    }

    public BigDecimal calcularTotalPendente() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.PENDENTE);
        return contas.stream().map(ContasReceber::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalVencido() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.VENCIDA);
        return contas.stream().map(ContasReceber::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalRecebido() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.PAGO);
        return contas.stream().map(ContasReceber::getValorRecebido).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void atualizarStatus(Long id, StatusFinanceiro novoStatus) {
        ContasReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        if (conta.getStatus() == StatusFinanceiro.PAGO && novoStatus == StatusFinanceiro.PENDENTE) {
            throw new RuntimeException("Não é possível alterar o status de uma conta PAGO para PENDENTE");
        }
        
        conta.setStatus(novoStatus);
        contaReceberRepository.save(conta);
    }
    public void cancelarConta(Long id){
        ContasReceber contasReceber = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (contasReceber.getStatus() == StatusFinanceiro.CANCELADA) {
            throw new RuntimeException("Não é possível cancelar uma conta já cancelada");
        }

        contasReceber.setStatus(StatusFinanceiro.CANCELADA);
        contasReceber.setValorRecebido(BigDecimal.ZERO);
        contaReceberRepository.save(contasReceber);
    }

    public void editarConta(Long id, Long clienteId, BigDecimal valor, BigDecimal valorRecebido,
                          LocalDate dataVencimento, LocalDate dataPagamento, FormaPagamento formaPagamento,
                          CondicaoPagamento condicaoPagamento, Integer numeroParcelas) {
        ContasReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Atualiza os campos permitidos
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

        contaReceberRepository.save(conta);
    }

}
