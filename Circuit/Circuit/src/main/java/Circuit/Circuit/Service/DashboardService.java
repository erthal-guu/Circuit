package Circuit.Circuit.Service;

import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Repository.ContaPagarRepository;
import Circuit.Circuit.Repository.ContaReceberRepository;
import Circuit.Circuit.Repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    public Double receitaTotal() {
        return dashboardRepository.calcularReceitaTotal();
    }
    public Double despesaTotal() {
        return dashboardRepository.calcularDespesaTotal();
    }
    public Double saldoLiquido(){
        Double receitaTotal = dashboardRepository.calcularReceitaTotal();
        Double despesaTotal = dashboardRepository.calcularDespesaTotal();
        return  receitaTotal - despesaTotal;
    }
    public Double lucroLiquidoTotal(){
        Double receitaTotal = dashboardRepository.calcularReceitaTotal();
        Double despesaTotal = dashboardRepository.calcularDespesaTotal();
        Double somaTotalEstoque = dashboardRepository.somaTotalEstoque();

        return receitaTotal - despesaTotal - somaTotalEstoque;
    }


    public List<Object[]> buscarFluxoCaixaMensal() {
        LocalDate dataFim = LocalDate.now();
        List<Object[]> fluxoCaixaList = new ArrayList<>();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate mes = dataFim.minusMonths(i);
            String nomeMes = mes.getMonth().toString().substring(0, 1) + mes.getMonth().toString().substring(1).toLowerCase();
            
            LocalDate inicioMes = YearMonth.from(mes).atDay(1);
            LocalDate fimMes = YearMonth.from(mes).atEndOfMonth();
            
            BigDecimal receitas = contaReceberRepository.buscarTotalReceitasPorPeriodo(inicioMes, fimMes);
            BigDecimal despesas = contaPagarRepository.buscarTotalDespesasPorPeriodo(inicioMes, fimMes);
            
            fluxoCaixaList.add(new Object[]{nomeMes, receitas != null ? receitas : BigDecimal.ZERO, despesas != null ? despesas : BigDecimal.ZERO});
        }
        
        return fluxoCaixaList;
    }

    public List<Object[]> buscarDistribuicaoDespesas() {
        return contaPagarRepository.buscarDistribuicaoDespesas();
    }



    public BigDecimal buscarTotalContasReceberProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate daqui30Dias = hoje.plusDays(30);
        BigDecimal total = contaReceberRepository.buscarTotalContasReceberProximos30Dias(hoje, daqui30Dias);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<ContasReceber> buscarContasReceberProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate daqui30Dias = hoje.plusDays(30);
        return contaReceberRepository.buscarContasReceberProximos30Dias(hoje, daqui30Dias).stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public BigDecimal buscarTotalContasPagarProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate daqui30Dias = hoje.plusDays(30);
        BigDecimal total = contaPagarRepository.buscarTotalContasPagarProximos30Dias(hoje, daqui30Dias);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<ContasPagar> buscarContasPagarProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate daqui30Dias = hoje.plusDays(30);
        return contaPagarRepository.buscarContasPagarProximos30Dias(hoje, daqui30Dias).stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
