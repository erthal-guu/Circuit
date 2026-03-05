package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContasPagarService {
    @Autowired
    ContaPagarRepository contaPagarRepository;

    public List<ContasPagar> listarContasPagar(){
        return contaPagarRepository.findAll();
    }

    public ContasPagar salvarContaPagar(ContasPagar contaPagar) {
        return contaPagarRepository.save(contaPagar);
    }

    public ContasPagar buscarPorId(Long id) {
        return contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));
    }

    public void deletarContaPagar(Long id) {
        ContasPagar conta = buscarPorId(id);
        contaPagarRepository.delete(conta);
    }

    public void pagarConta(Long id, BigDecimal valorPago, LocalDate dataPagamento, FormaPagamento formaPagamento) {
        ContasPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            throw new RuntimeException("Não é possível pagar uma conta com status PAGO");
        }

        BigDecimal valorTotal = conta.getValor();
        if (valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor pago deve ser maior que zero");
        }
        if (valorPago.compareTo(valorTotal) > 0) {
            throw new RuntimeException("Valor pago não pode ser maior que o valor total da conta");
        }

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

        contaPagarRepository.save(conta);
    }

    public BigDecimal calcularTotalPendente() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.PENDENTE);
        return contas.stream().map(ContasPagar::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalVencido() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.VENCIDA);
        return contas.stream().map(ContasPagar::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalPago() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.PAGO);
        return contas.stream().map(ContasPagar::getValorPago).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void atualizarStatus(Long id, StatusFinanceiro novoStatus) {
        ContasPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta.getStatus() == StatusFinanceiro.PAGO && novoStatus == StatusFinanceiro.PENDENTE) {
            throw new RuntimeException("Não é possível alterar o status de uma conta PAGO para PENDENTE");
        }

        conta.setStatus(novoStatus);
        contaPagarRepository.save(conta);
    }
}
