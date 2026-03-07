package Circuit.Circuit.Controller;

import Circuit.Circuit.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/financeiro")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String abrirDashboards(Model model) {
        model.addAttribute("receitaTotal", dashboardService.receitaTotal());
        model.addAttribute("saldoLiquido", dashboardService.saldoLiquido());
        model.addAttribute("despesaTotal", dashboardService.despesaTotal());
        model.addAttribute("lucroliquido", dashboardService.lucroLiquidoTotal());
        model.addAttribute("fluxoCaixaData", dashboardService.buscarFluxoCaixaMensal());
        model.addAttribute("distribuicaoDespesasData", dashboardService.buscarDistribuicaoDespesas());
        model.addAttribute("contasReceberTotal", dashboardService.buscarTotalContasReceberProximos30Dias());
        model.addAttribute("contasReceberList", dashboardService.buscarContasReceberProximos30Dias());
        model.addAttribute("contasPagarTotal", dashboardService.buscarTotalContasPagarProximos30Dias());
        model.addAttribute("contasPagarList", dashboardService.buscarContasPagarProximos30Dias());

        return "financeiro";
    }
}