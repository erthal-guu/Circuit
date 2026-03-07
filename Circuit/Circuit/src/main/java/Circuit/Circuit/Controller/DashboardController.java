package Circuit.Circuit.Controller;

import Circuit.Circuit.Dto.DistribuicaoDespesasDto;
import Circuit.Circuit.Dto.FluxoCaixaMensalDto;
import Circuit.Circuit.Dto.ResumoContasDto;
import Circuit.Circuit.Model.ContasPagar;
import Circuit.Circuit.Model.ContasReceber;
import Circuit.Circuit.Model.Enum.StatusFinanceiro;
import Circuit.Circuit.Service.ContaReceberService;
import Circuit.Circuit.Service.ContasPagarService;
import Circuit.Circuit.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/financeiro")
public class DashboardController {
    @Autowired
    private ContaReceberService contaReceberService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String abrirDashboards(Model model) {
        Double receitaTotal = dashboardService.receitaTotal();
        Double despesaTotal = dashboardService.despesaTotal();
        Double saldoLiquido = dashboardService.saldoLiquido();
        Double lucroLiquido = dashboardService.lucroLiquidoTotal();

        model.addAttribute("receitaTotal", receitaTotal);
        model.addAttribute("saldoLiquido", saldoLiquido);
        model.addAttribute("despesaTotal", despesaTotal);
        model.addAttribute("lucroliquido", lucroLiquido);
        return "financeiro";
    }

    @GetMapping("/api/fluxo-caixa")
    @ResponseBody
    public ResponseEntity<List<FluxoCaixaMensalDto>> getFluxoCaixaMensal() {
        List<FluxoCaixaMensalDto> fluxoCaixa = dashboardService.buscarFluxoCaixaMensal();
        return ResponseEntity.ok(fluxoCaixa);
    }

    @GetMapping("/api/distribuicao-despesas")
    @ResponseBody
    public ResponseEntity<List<DistribuicaoDespesasDto>> getDistribuicaoDespesas() {
        List<DistribuicaoDespesasDto> distribuicao = dashboardService.buscarDistribuicaoDespesas();
        return ResponseEntity.ok(distribuicao);
    }

    @GetMapping("/api/contas-receber")
    @ResponseBody
    public ResponseEntity<ResumoContasDto> getContasReceber() {
        ResumoContasDto resumo = dashboardService.buscarResumoContasReceberProximos30Dias();
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/api/contas-pagar")
    @ResponseBody
    public ResponseEntity<ResumoContasDto> getContasPagar() {
        ResumoContasDto resumo = dashboardService.buscarResumoContasPagarProximos30Dias();
        return ResponseEntity.ok(resumo);
    }
}