package Circuit.Circuit.Service;

import Circuit.Circuit.Dto.ContaPagarDashboardDto;
import Circuit.Circuit.Dto.ContaReceberDashboardDto;
import Circuit.Circuit.Dto.DistribuicaoDespesasDto;
import Circuit.Circuit.Dto.FluxoCaixaMensalDto;
import Circuit.Circuit.Dto.ResumoContasDto;
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

    public List<FluxoCaixaMensalDto> buscarFluxoCaixaMensal() {
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusMonths(5).withDayOfMonth(1);
        
        List<FluxoCaixaMensalDto> fluxoCaixaList = new ArrayList<>();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate mes = dataFim.minusMonths(i);
            String nomeMes = mes.getMonth().toString().substring(0, 1) + mes.getMonth().toString().substring(1).toLowerCase();
            Integer mesNumero = mes.getMonthValue();
            
            LocalDate inicioMes = YearMonth.from(mes).atDay(1);
            LocalDate fimMes = YearMonth.from(mes).atEndOfMonth();
            
            BigDecimal receitas = contaReceberRepository.buscarTotalReceitasPorPeriodo(inicioMes, fimMes);
            BigDecimal despesas = contaPagarRepository.buscarTotalDespesasPorPeriodo(inicioMes, fimMes);
            
            fluxoCaixaList.add(new FluxoCaixaMensalDto(nomeMes, receitas != null ? receitas : BigDecimal.ZERO, despesas != null ? despesas : BigDecimal.ZERO));
        }
        
        return fluxoCaixaList;
    }

    public List<DistribuicaoDespesasDto> buscarDistribuicaoDespesas() {
        List<Object[]> resultados = contaPagarRepository.buscarDistribuicaoDespesas();
        List<DistribuicaoDespesasDto> distribuicaoList = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            String categoria = (String) resultado[0];
            BigDecimal total = resultado[1] != null ? new BigDecimal(resultado[1].toString()) : BigDecimal.ZERO;
            distribuicaoList.add(new DistribuicaoDespesasDto(categoria, total));
        }
        
        return distribuicaoList;
    }

    public ResumoContasDto buscarResumoContasReceberProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate daqui30Dias = hoje.plusDays(30);
        
        Long pendentes = contaReceberRepository.contarContasPendentesProximos30Dias(hoje, daqui30Dias);
        Long vencidas = contaReceberRepository.contarContasVencidas(hoje);
        BigDecimal total = contaReceberRepository.buscarTotalContasReceberProximos30Dias(hoje, daqui30Dias);
        
        List<ContasReceber> contas = contaReceberRepository.buscarContasReceberProximos30Dias(hoje, daqui30Dias);
        List<ContaReceberDashboardDto> contasDto = contas.stream()
                .limit(10)
                .map(this::converterParaContaReceberDashboardDto)
                .collect(Collectors.toList());
        
        return new ResumoContasDto(
                pendentes != null ? pendentes.intValue() : 0,
                vencidas != null ? vencidas.intValue() : 0,
                total != null ? total : BigDecimal.ZERO,
                contasDto
        );
    }

    public ResumoContasDto buscarResumoContasPagarProximos30Dias() {
        LocalDate hoje = LocalDate.now();
        LocalDate daqui30Dias = hoje.plusDays(30);
        
        Long pendentes = contaPagarRepository.contarContasPendentesProximos30Dias(hoje, daqui30Dias);
        Long vencidas = contaPagarRepository.contarContasVencidas(hoje);
        BigDecimal total = contaPagarRepository.buscarTotalContasPagarProximos30Dias(hoje, daqui30Dias);
        
        List<ContasPagar> contas = contaPagarRepository.buscarContasPagarProximos30Dias(hoje, daqui30Dias);
        List<ContaPagarDashboardDto> contasDto = contas.stream()
                .limit(10)
                .map(this::converterParaContaPagarDashboardDto)
                .collect(Collectors.toList());
        
        return new ResumoContasDto(
                pendentes != null ? pendentes.intValue() : 0,
                vencidas != null ? vencidas.intValue() : 0,
                total != null ? total : BigDecimal.ZERO,
                contasDto
        );
    }

    private ContaReceberDashboardDto converterParaContaReceberDashboardDto(ContasReceber conta) {
        String nomeCliente = conta.getCliente() != null ? conta.getCliente().getNome() : "Cliente não informado";
        String iniciais = extrairIniciais(nomeCliente);
        
        return new ContaReceberDashboardDto(
                conta.getId(),
                nomeCliente,
                conta.getOrigem(),
                conta.getOrigemId(),
                conta.getValor(),
                conta.getDataVencimento(),
                conta.getStatus() != null ? conta.getStatus().toString() : "PAGO",
                iniciais
        );
    }

    private ContaPagarDashboardDto converterParaContaPagarDashboardDto(ContasPagar conta) {
        String nomeFornecedor = conta.getFornecedor() != null ? conta.getFornecedor().getNomeFantasia() : "Fornecedor não informado";
        String iniciais = extrairIniciais(nomeFornecedor);
        
        return new ContaPagarDashboardDto(
                conta.getId(),
                nomeFornecedor,
                conta.getDescricao(),
                conta.getOrigem(),
                conta.getOrigemId(),
                conta.getValor(),
                conta.getDataVencimento(),
                conta.getStatus() != null ? conta.getStatus().toString() : "PAGO",
                iniciais
        );
    }

    private String extrairIniciais(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "??";
        }
        
        String[] partes = nome.trim().split("\\s+");
        if (partes.length >= 2) {
            return (partes[0].charAt(0) + "" + partes[partes.length - 1].charAt(0)).toUpperCase();
        } else if (partes.length == 1 && partes[0].length() >= 2) {
            return (partes[0].charAt(0) + "" + partes[0].charAt(1)).toUpperCase();
        } else {
            return partes[0].substring(0, 1).toUpperCase() + "?";
        }
    }
}
