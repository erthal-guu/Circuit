package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Model.OrdemServico;
import Circuit.Circuit.Model.Venda;
import Circuit.Circuit.Repository.ContaReceberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaReceberService {
    @Autowired
    ContaReceberRepository contaReceberRepository;

    public void gerarContaReceberParaVenda(Venda venda ) {
        boolean VendaJaGerada = contaReceberRepository.existsByOrigemAndOrigemId("VENDA", venda.getId());
        if (VendaJaGerada){
            return;
        }
        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setOrigem("VENDA");
        contasReceber.setOrigemId(venda.getId());
        contasReceber.setValor(venda.getValorTotal());
        contasReceber.setDataVencimento(venda.getDataVenda().plusDays(30));
        contasReceber.setStatus(StatusFinanceiro.PENDENTE);
        contasReceber.setCliente(venda.getCliente());
        contaReceberRepository.save(contasReceber);
    }

    public void gerarContaReceberParaOS(OrdemServico ordemServico) {
        boolean OSjaGerada = contaReceberRepository.existsByOrigemAndOrigemId("ORDEM_SERVICO", ordemServico.getId());
        if (OSjaGerada) {
            return;
        }
        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setOrigem("ORDEM_SERVICO");
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

    public BigDecimal calcularTotalPendente() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.PENDENTE);
        return contas.stream().map(ContasReceber::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalVencido() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.VENCIDO);
        return contas.stream().map(ContasReceber::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalRecebido() {
        List<ContasReceber> contas = contaReceberRepository.findByStatus(StatusFinanceiro.PAGO);
        return contas.stream().map(ContasReceber::getValorRecebido).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
