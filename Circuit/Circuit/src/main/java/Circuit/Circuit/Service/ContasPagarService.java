package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.Enum.CondicaoPagamento;
import Circuit.Circuit.Model.Enum.FormaPagamento;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Model.Fornecedor;
import Circuit.Circuit.Model.Pedido;
import Circuit.Circuit.Repository.ContaPagarRepository;
import Circuit.Circuit.Repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContasPagarService {
    @Autowired
    ContaPagarRepository contaPagarRepository;
    
    @Autowired
    FornecedorRepository fornecedorRepository;

    public void gerarContaPagarParaPedido(Pedido pedido) {
        boolean pedidoJaGerado = contaPagarRepository.existsByOrigemAndOrigemId("Pedido", pedido.getId());
        if (pedidoJaGerado) {
            return;
        }
        ContasPagar contasPagar = new ContasPagar();
        contasPagar.setOrigem("Pedido");
        contasPagar.setOrigemId(pedido.getId());
        contasPagar.setValor(pedido.getValorTotal());
        contasPagar.setDataVencimento(pedido.getDataPedido().plusDays(30));
        contasPagar.setStatus(StatusFinanceiro.PENDENTE);
        contasPagar.setFornecedor(pedido.getFornecedor());
        contasPagar.setDescricao("Pedido #" + pedido.getCodigo());
        contaPagarRepository.save(contasPagar);
    }

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

    public BigDecimal calcularTotalPago() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.PAGO);
        return contas.stream().map(ContasPagar::getValorPago).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalCancelado() {
        List<ContasPagar> contas = contaPagarRepository.findByStatus(StatusFinanceiro.CANCELADA);
        return contas.stream().map(ContasPagar::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cancelarConta(Long id) {
        ContasPagar contasPagar = contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (contasPagar.getStatus() == StatusFinanceiro.CANCELADA) {
            throw new RuntimeException("Não é possível cancelar uma conta já cancelada");
        }

        if (contasPagar.getStatus() == StatusFinanceiro.PAGO) {
            throw new RuntimeException("Não é possível cancelar uma conta já paga");
        }

        contasPagar.setStatus(StatusFinanceiro.CANCELADA);
        contasPagar.setValorPago(BigDecimal.ZERO);
        contaPagarRepository.save(contasPagar);
    }

    public void editarConta(Long id, Long fornecedorId, BigDecimal valor, BigDecimal valorPago,
                          LocalDate dataVencimento, LocalDate dataPagamento, FormaPagamento formaPagamento,
                          CondicaoPagamento condicaoPagamento, Integer numeroParcelas) {
        ContasPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta.getStatus() == StatusFinanceiro.PAGO) {
            throw new RuntimeException("Não é possível editar uma conta com status PAGO");
        }
        if (conta.getStatus() == StatusFinanceiro.PARCIAL) {
            throw new RuntimeException("Não é possível editar uma conta com status PARCIAL");
        }
        if (conta.getStatus() == StatusFinanceiro.CANCELADA) {
            throw new RuntimeException("Não é possível editar uma conta cancelada");
        }

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

        contaPagarRepository.save(conta);
    }
}
